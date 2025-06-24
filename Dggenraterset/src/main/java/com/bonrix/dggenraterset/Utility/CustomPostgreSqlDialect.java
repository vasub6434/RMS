package com.bonrix.dggenraterset.Utility;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL9Dialect;

public class CustomPostgreSqlDialect extends PostgreSQL9Dialect {
 
    public CustomPostgreSqlDialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}
