package stroom.data.store.impl.fs.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

class ConnectionProvider extends HikariDataSource {
    ConnectionProvider(final HikariConfig configuration) {
        super(configuration);
    }
}
