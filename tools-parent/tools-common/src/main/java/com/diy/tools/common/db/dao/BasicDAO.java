package com.diy.tools.common.db.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;


public abstract class BasicDAO {

	@Resource(name = "daoSupportHibernate")
	private DAOSupport daoSupport;

	public BasicDAO() {
	}

	public Serializable insert(Object obj) throws RuntimeException {
		try {
			return daoSupport.insert(obj);
		} catch (Exception e) {
			throw new RuntimeException("insert failed!", e);
		}
	}

	public Serializable insert2(Object obj) throws RuntimeException {
		try {
			return daoSupport.insert(obj);
		} catch (Exception e) {
			throw new RuntimeException("insert failed!", e);
		}
	}

	public void update(Object obj) throws RuntimeException {
		try {
			daoSupport.update(obj);
		} catch (Exception e) {
			throw new RuntimeException("update failed!", e);
		}
	}

	public int update(String hql, Object values[]) throws RuntimeException {
		try {
			return daoSupport.update(hql, values);
		} catch (Exception e) {
			throw new RuntimeException("update failed!", e);
		}
	}

	public void delete(Object obj) throws RuntimeException {
		try {
			daoSupport.delete(obj);
		} catch (Exception e) {
			throw new RuntimeException("delete failed!", e);
		}
	}

	public void delete(Class c, Serializable s) throws RuntimeException {
		try {
			daoSupport.delete(c, s);
		} catch (Exception e) {
			throw new RuntimeException("delete failed!", e);
		}
	}

	public Object load(Class c, Serializable s) throws RuntimeException {
		try {
			return daoSupport.load(c, s);
		} catch (Exception e) {
			throw new RuntimeException("load failed!", e);
		}

	}

	public Object loadAll(Class c) throws RuntimeException {
		try {
			return daoSupport.loadAll(c);
		} catch (Exception e) {
			throw new RuntimeException("load failed!", e);
		}

	}

	public int getCount(String sql, List values) throws RuntimeException {
		Object obj;
		List list = null;
		try {
			list = daoSupport.createSQLQuery(sql, values, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("count failed!", e);
		}
		obj = list.get(0);
		if (obj == null)
			return 0;

		return Integer.parseInt((String) obj);
	}

	public List createQuery(String hql, List values, int firstRow, int maxRows) throws RuntimeException {
		try {
			return daoSupport.createQuery(hql, values, firstRow, maxRows);
		} catch (Exception e) {
			throw new RuntimeException("hql query failed!", e);
		}

	}

	public List createSQLQuery(String sql, List values, int firstRow, int maxRows) throws RuntimeException {
		try {
			return daoSupport.createSQLQuery(sql, values, firstRow, maxRows);
		} catch (Exception e) {
			throw new RuntimeException("sql query failed!", e);
		}
	}

	public int execute(String sql, List values) throws RuntimeException {
		try {
			return daoSupport.execute(sql, values);
		} catch (Exception e) {
			throw new RuntimeException("sql execute failed!", e);
		}
	}

	public DAOSupport getDaoSupport() throws RuntimeException {
		if (daoSupport == null)
			throw new RuntimeException("The property 'daoSupport' is null due to no setting the property with a implement when construct the BasicDao.");
		else
			return daoSupport;
	}

	public void setDaoSupport(DAOSupport daoSupport) {
		if (daoSupport == null) {
			throw new RuntimeException("Setting the property 'daoSupport' with a null!");
		} else {
			this.daoSupport = daoSupport;
			return;
		}
	}

	public Session getSession() {
		return daoSupport.getHbSession();
	}

	/**
	 * 杈撳叆琛ㄥ悕銆佸瓧娈靛悕浠ュ強瀛楁闀垮害鍒欏彲鑾峰彇鐢熸垚鐨処D
	 */
	public String generateId(String table, String field, int filedLen, String prefix) {
		StringBuilder ret = new StringBuilder();
		String sql = "";
		if (null != prefix && !"".equals(prefix))
			sql = "select max(to_number(substr(" + field + ", " + (prefix.length() + 1) + "))) from " + table + " where 1=1 order by " + field + " desc";
		else
			sql = "select max(to_number(" + field + ")) from " + table + " where 1=1 order by " + field + " desc";
		Object obj = createSQLQuery(sql, null, 0, 0).get(0);

		int prefixLen = 0;
		if (null != prefix && !"".equals(prefix)) {
			prefixLen = prefix.trim().length();
			ret.append(prefix.trim());
		}

		if (obj == null || "".equals(obj.toString()))
			obj = 0;

		String result = new BigInteger(obj.toString()).add(new BigInteger("1")).toString();
		int missLen = filedLen - result.length() - prefixLen;
		if (missLen < 0)
			return null;
		for (int i = 0; i < missLen; i++) {
			ret.append("0");
		}

		ret.append(result);

		return ret.toString();
	}

	/**
	 * 
	 * 杈撳叆琛ㄥ悕銆佸瓧娈靛悕浠ュ強瀛楁闀垮害鍒欏彲鑾峰彇鐢熸垚鐨処D,鑾峰彇杞鐧昏娴佹按鍙�
	 */
	public String generateIdAssignNo(String table, String field, int filedLen, String prefix) {
		StringBuilder ret = new StringBuilder();
		String sqldate = "select to_char(sysdate,'yyyyMMdd') from dual";
		String todayDate = (String) createSQLQuery(sqldate, null, 0, 0).get(0);// 鍙栨暟鎹簱鏃堕棿

		String sql = "";
		sql = "select max(" + field + ") from " + table + " where 1=1 and substr(" + field + ",0,8)= '" + todayDate + "'";

		Object obj = null;
		List list = createSQLQuery(sql, null, 0, 0);
		if (list.size() > 0) {
			obj = list.get(0);
		}

		if (obj == null || "".equals(obj.toString()))
			obj = 0;

		String assignDate = "";
		StringBuffer sb = new StringBuffer();
		sb.append(todayDate);
		if (obj.toString().length() > 8) {
			assignDate = obj.toString().substring(0, 8);
		}

		String result = "";
		if (assignDate.equals(todayDate)) {
			obj = obj.toString().substring(8);
			result = new BigInteger(obj.toString()).add(new BigInteger("1")).toString();
		} else {
			obj = 0;
			result = new BigInteger(obj.toString()).add(new BigInteger("1")).toString();
		}

		int missLen = filedLen - result.length() - 8;// 8浣嶆棩鏈熼暱搴�
		for (int i = 0; i < missLen; i++) {
			ret.append("0");
		}

		ret.append(result);

		return sb.append(ret.toString()).toString();
	}

	/**
	 * 杈撳叆琛ㄥ悕銆佸瓧娈靛悕浠ュ強瀛楁闀垮害銆乻ql鏌ヨ鏉′欢鍒欏彲鑾峰彇鐢熸垚鐨処D
	 */
	public String generateId(String table, String field, int filedLen, String prefix, String condition) {
		StringBuilder ret = new StringBuilder();
		String sql = "";
		if (null != prefix && !"".equals(prefix)) {
			sql = "select max(to_number(substr(" + field + ", " + (prefix.length() + 1) + "))) from " + table + " where 1=1  ";
		} else {
			sql = "select max(to_number(" + field + ")) from " + table + " where 1=1 ";
		}
		sql = sql + "and regexp_like(substr(user_id,11,4),'^\\d{4}$')";
		if (null != condition && !"".equalsIgnoreCase(condition))
			sql += condition;

		sql += " order by " + field + " desc";

		Object obj = createSQLQuery(sql, null, 0, 0).get(0);

		int prefixLen = 0;
		if (null != prefix && !"".equals(prefix)) {
			prefixLen = prefix.trim().length();
			ret.append(prefix.trim());
		}

		if (obj == null || "".equals(obj.toString()))
			obj = 0;

		String result = new BigInteger(obj.toString()).add(new BigInteger("1")).toString();
		int missLen = filedLen - result.length() - prefixLen;
		if (missLen < 0)
			return null;
		for (int i = 0; i < missLen; i++) {
			ret.append("0");
		}
		ret.append(result);
		return ret.toString();
	}

	/**
	 * 杈撳叆琛ㄥ悕銆佸瓧娈靛悕 鑾峰彇鐢熸垚鐨処D
	 */
	public long generateId(String table, String field) {
		String sql = "select max(to_number(" + field + ")) from " + table + " where 1=1 order by " + field + " desc";
		Object obj = createSQLQuery(sql, null, 0, 0).get(0);

		if (obj == null || "".equals(obj.toString()))
			obj = 0;

		long result = Long.valueOf(obj.toString()) + 1;

		return result;
	}

	/**
	 * 杈撳叆琛ㄥ悕銆佸瓧娈靛悕 鑾峰彇鐢熸垚鐨処D
	 */
	public String generateStrId(String table, String field) {
		String sql = "select max(to_number(" + field + ")) from " + table + " where 1=1 order by " + field + " desc";
		Object obj = createSQLQuery(sql, null, 0, 0).get(0);

		if (obj == null || "".equals(obj.toString()))
			obj = 0;

		long result = Long.valueOf(obj.toString()) + 1;

		return String.valueOf(result);
	}

	public void executeHql(String hql, List value) throws RuntimeException {
		daoSupport.executeHql(hql, value);
	}

	public Object getObjectById(Class c, Serializable s) throws RuntimeException {
		try {
			return daoSupport.getObjectById(c, s);
		} catch (Exception e) {
			throw new RuntimeException("load failed!", e);
		}
	}

	public List querySQL(String sql, Object[] values, int firstRow, int maxRows) throws RuntimeException {
		try {
			return daoSupport.querySQL(sql, values, firstRow, maxRows);
		} catch (Exception e) {
			throw new RuntimeException("sql query failed!", e);
		}
	}

	public Object getSingleObj(String sql, Object[] values) throws RuntimeException {
		Object obj;
		List list = null;
		try {
			list = daoSupport.querySQL(sql, values, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("count failed!", e);
		}
		
		obj = list.get(0);
		return obj;
	}

}
