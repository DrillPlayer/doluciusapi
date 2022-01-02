package de.drillplayer.doluciusapi.mysql;

import de.drillplayer.doluciusapi.DoluciusAPIMain;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {

    private DoluciusAPIMain plugin;

    public SQLGetter(DoluciusAPIMain plugin) {
        this.plugin = plugin;
    }

    public void createCoinTable() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS doluciusapi " + "(NAME VARCHAR(100), UUID VARCHAR(100), COINS INT (100), PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createBanTable() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bantable " + "(NAME VARCHAR(100), UUID VARCHAR(100), REASON VARCHAR(100), TIME INT(100), PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if (!existsCoins(uuid)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO doluciusapi (NAME,UUID) VALUES (?,?)");
                ps.setString(1, player.getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createBanPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if (!existsBan(uuid)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO bantable (NAME,UUID) VALUES (?,?)");
                ps.setString(1, player.getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean existsCoins(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM doluciusapi WHERE UUID =?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean existsBan(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM bantable WHERE UUID =?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void addCoins(UUID uuid, int coins) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE doluciusapi SET COINS=? WHERE UUID=?");
            ps.setInt(1, getCoins(uuid) + coins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addBan(UUID uuid, String reason, int time) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE bantable SET REASON=? WHERE UUID=?");
            PreparedStatement ps1 = plugin.SQL.getConnection().prepareStatement("UPDATE bantable SET TIME=? WHERE UUID=?");
            ps.setString(1, reason);
            ps.setString(2, uuid.toString());
            ps1.setInt(1, time);
            ps1.setString(2, uuid.toString());
            ps.executeUpdate();
            ps1.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int getCoins(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT COINS FROM doluciusapi WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int coins = 0;

            if (rs.next()) {
                coins = rs.getInt("COINS");
                return coins;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
