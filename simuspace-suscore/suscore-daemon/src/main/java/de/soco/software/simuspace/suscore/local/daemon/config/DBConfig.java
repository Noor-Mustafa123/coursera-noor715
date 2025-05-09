package de.soco.software.simuspace.suscore.local.daemon.config;

import javax.sql.DataSource;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 * The Class DBConfig is for sqlite location and driver
 *
 * @author noman
 */
@Configuration
public class DBConfig {

    /**
     * Data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {

        String userHome = "user.home";
        String path = System.getProperty( userHome );
        String pathURL = "jdbc:sqlite:" + path + File.separator + "SIMuSPACE" + File.separator + "sim_local.db";

        SQLiteDataSource ds = new SQLiteDataSource( getSQLiteConfig() );
        ds.setUrl( pathURL );
        return ds;
    }

    public SQLiteConfig getSQLiteConfig() {
        SQLiteConfig config = new SQLiteConfig();
        config.setJournalMode( SQLiteConfig.JournalMode.WAL );
        config.setSynchronous( SQLiteConfig.SynchronousMode.FULL );
        return config;
    }

}