package com.uestc.news.data;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

/**
 * 这个java类用于生成数据库，根据entity包下面的实体类映射数据表
 * 
 * @author yuer
 * 
 */
public class GenerateTable {
	@Test
	public static void main(String[] args) {
		Configuration cfg = new Configuration().configure();
		SchemaExport export = new SchemaExport(cfg);
		export.create(true, true);
	}
}
