// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DAOSupportHibernate3Impl.java

package com.diy.tools.common.db.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.diy.tools.common.db.dao.DAOSupport;
import com.diy.tools.common.utils.LogerUtil;

public class DAOSupportHibernate3Impl extends HibernateDaoSupport implements DAOSupport {

	public DAOSupportHibernate3Impl() {
		logerUtil = new com.diy.tools.common.utils.LogerUtil(DAOSupportHibernate3Impl.class);
	}

	public List createQuery(String hql, List values, int firstRow, int maxRows) throws RuntimeException {
		logerUtil.warn((new StringBuilder("hql:")).append(hql).toString());
		Query query;
		if (hql == null || hql.equals(""))
			throw new RuntimeException("hql\u8BED\u53E5\u4E0D\u80FD\u4E3A\u7A7A\uFF01");
		Session session = getHbSession();
		query = null;
		if (values != null && values.size() > 0) {
			query = session.createQuery(hql);
			for (int i = 0; i < values.size(); i++) {
				Object obj = values.get(i);
				query.setParameter(i, obj);
			}

		} else {
			query = session.createQuery(hql);
		}
		if (firstRow > 0) {
			query.setFirstResult(firstRow);
			logerUtil.warn((new StringBuilder("first row: ")).append(firstRow).toString());
		}
		if (maxRows > 0) {
			query.setMaxResults(maxRows);
			logerUtil.warn((new StringBuilder("max rows: ")).append(maxRows).toString());
		}

		List list;
		try {
			list = query.list();
		} catch (RuntimeException re) {
			logerUtil.error("query failed!", re);
			throw re;
		}

		return list;
	}

	public List createSQLQuery(String sql, List values, int firstRow, int maxRows) throws RuntimeException {
		List result;
		logerUtil.warn("sql:" + sql.toString());
		result = new ArrayList();
		List list;
		if (sql == null || sql.equals(""))
			throw new RuntimeException("sql\u8BED\u53E5\u4E0D\u80FD\u4E3A\u7A7A\uFF01");
		Session session = getHbSession();
		Query query = null;
		if (values != null && values.size() > 0) {
			query = session.createSQLQuery(sql);
			for (int i = 0; i < values.size(); i++) {
				Object obj = values.get(i);
				query.setParameter(i, obj);
			}

		} else {
			query = session.createSQLQuery(sql);
		}
		if (firstRow > 0) {
			query.setFirstResult(firstRow);
			logerUtil.warn((new StringBuilder("first row: ")).append(firstRow).toString());
		}
		if (maxRows > 0) {
			query.setMaxResults(maxRows);
			logerUtil.warn((new StringBuilder("max rows: ")).append(maxRows).toString());
		}
		try {
			result = query.list();
		} catch (RuntimeException re) {
			logerUtil.error("query failed!", re);
			throw re;
		}

		if (firstRow <= 0 && maxRows <= 0)
			return result;
		list = new ArrayList();
		for (int i = 0; i < result.size(); i++) {
			Object objs[] = (Object[]) result.get(i);
			Object newObjs[] = new Object[objs.length - 1];
			for (int j = 0; j < objs.length; j++)
				if (j > 0)
					newObjs[j - 1] = objs[j];

			list.add(((Object) (newObjs)));
		}

		return list;
	}

	public void delete(Object obj) throws RuntimeException {
		logerUtil.warn((new StringBuilder("delete:")).append(getClassName(obj)).append(" instance.").toString());
		try {
			getHibernateTemplate().delete(obj);
			logerUtil.warn("delete successful!");
		} catch (RuntimeException re) {
			logerUtil.error("delete failed!", re);
			throw re;
		}
	}

	public void delete(Class c, Serializable s) {
		Object obj = load(c, s);
		delete(obj);
	}

	public void delete(List list) throws RuntimeException {
		logerUtil.warn("delete with a list.");
		try {
			if (list == null)
				throw new RuntimeException("\u503C\u5BF9\u8C61\u5217\u8868\u4E0D\u80FD\u4E3Anull\uFF01");
			getHibernateTemplate().deleteAll(list);
			logerUtil.warn("delete successful!");
		} catch (RuntimeException re) {
			logerUtil.error("delete failed!", re);
			throw re;
		}
	}

	public void executeHql(String hql, List values) throws RuntimeException {
		Session session;
		Query query;
		logerUtil.warn("executeHql with a list.");
		try {
			if (hql == null)
				throw new RuntimeException("\u503C\u5BF9\u8C61\u5217\u8868\u4E0D\u80FD\u4E3Anull\uFF01");
			session = getHbSession();// getHibernateTemplate().getSessionFactory().openSession();
			// Transaction tx = session.getTransaction();
			// session.beginTransaction();
			query = session.createQuery(hql);

			for (int i = 0; i < values.size(); i++) {
				Object obj = values.get(i);
				query.setParameter(i, obj);
			}
			try {

				query.executeUpdate();

				// tx.commit();

			} catch (RuntimeException re) {
				logerUtil.error("execute sql failed", re);
				throw re;
			} finally {
				// session.close();
			}
			logerUtil.warn("delete successful!");
		} catch (RuntimeException re) {
			logerUtil.error("delete failed!", re);
			throw re;
		}
	}

	public Serializable insert(Object obj) throws RuntimeException {
		logerUtil.warn((new StringBuilder("insert ")).append(getClassName(obj)).append(" instance.").toString());

		try {
			return getHibernateTemplate().save(obj);
		} catch (RuntimeException re) {
			logerUtil.error("insert failed!", re);
			throw re;
		}
	}

	public Object load(Class c, Serializable s) throws RuntimeException {
		logerUtil.warn((new StringBuilder("load ")).append(getClassName(c)).append(" instance with id: ").append(s).append(".").toString());
		if (c == null)
			throw new RuntimeException("\u7C7B\u4E0D\u80FD\u4E3Anull\uFF01");
		if (s == null)
			throw new RuntimeException("\u4E3B\u952E\u503C\u4E0D\u80FD\u4E3Anull\uFF01");

		try {
			return getHibernateTemplate().load(c, s);
		} catch (RuntimeException re) {
			logerUtil.error("load failed!", re);
			throw re;
		}

	}

	public void update(Object obj) throws RuntimeException {
		logerUtil.warn((new StringBuilder("update ")).append(getClassName(obj)).append(" instance.").toString());
		try {
			getHibernateTemplate().saveOrUpdate(obj);
			logerUtil.warn("update successful!");
		} catch (RuntimeException re) {
			logerUtil.error("update failed!", re);
			throw re;
		}
	}

	public int update(String hql, Object values[]) throws RuntimeException {
		logerUtil.warn((new StringBuilder("update: ")).append(hql).append(";").toString());

		try {
			return getHibernateTemplate().bulkUpdate(hql, values);
		} catch (RuntimeException re) {
			logerUtil.error("update failed!", re);
			throw re;
		}
	}

	public void update(List list) throws RuntimeException {
		logerUtil.warn("update with list.");
		try {
			getHibernateTemplate().saveOrUpdate(list);
			logerUtil.warn("update successful!");
		} catch (RuntimeException re) {
			logerUtil.error("update failed!", re);
			throw re;
		}
	}

	public int execute(String sql, List values) throws RuntimeException {
		logerUtil.warn((new StringBuilder("execute: ")).append(sql).append(";").toString());
		Session session;
		Query query;
		if (sql == null || sql.equals(""))
			throw new RuntimeException("sql\u8BED\u53E5\u4E0D\u80FD\u4E3A\u7A7A\uFF01");
		session = getHbSession();
		query = null;
		if (values == null || values.size() <= 0)
			return -1;
		query = session.createSQLQuery(sql);
		for (int i = 0; i < values.size(); i++) {
			Object obj = values.get(i);
			query.setParameter(i, obj);
		}

		try {
			return query.executeUpdate();
		} catch (RuntimeException re) {
			logerUtil.error("execute sql failed", re);
			throw re;
		}

	}

	private String getClassName(String className) {
		return className.substring(className.lastIndexOf('.') + 1);
	}

	private String getClassName(Object obj) {
		return obj != null ? getClassName(obj.getClass().getName()) : "";
	}

	public Session getHbSession() {
		return super.getHibernateTemplate().getSessionFactory().getCurrentSession();
	}

	public Session getSpSession() {
		return super.getSession();
	}

	public Object getObjectById(Class c, Serializable s) throws RuntimeException {
		logerUtil.warn((new StringBuilder("load ")).append(getClassName(c)).append(" instance with id: ").append(s).append(".").toString());
		if (c == null)
			throw new RuntimeException("\u7C7B\u4E0D\u80FD\u4E3Anull\uFF01");
		if (s == null)
			throw new RuntimeException("\u4E3B\u952E\u503C\u4E0D\u80FD\u4E3Anull\uFF01");

		try {
			return getHibernateTemplate().get(c, s);
		} catch (RuntimeException re) {
			logerUtil.error("load failed!", re);
			throw re;
		}

	}

	private LogerUtil logerUtil;

	public Object loadAll(Class c) throws RuntimeException {
		logerUtil.warn((new StringBuilder("load ")).append(getClassName(c)).append(" instance with id: ").toString());
		if (c == null)
			throw new RuntimeException("\u7C7B\u4E0D\u80FD\u4E3Anull\uFF01");
		try {
			return getHibernateTemplate().loadAll(c);
		} catch (RuntimeException re) {
			logerUtil.error("load failed!", re);
			throw re;
		}

	}

	public List querySQL(String sql, Object[] values, int firstRow, int maxRows) {
		logerUtil.warn("createSQLQuery--sql:" + sql);
		List result = new ArrayList();
		try {
			if (sql == null || sql.equals(""))
				throw new RuntimeException("sql语句不能为空");

			Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = null;
			if (values != null && values.length > 0) {
				query = session.createSQLQuery(sql);
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			} else {
				query = session.createSQLQuery(sql);
			}

			if (firstRow > 0) {
				query.setFirstResult(firstRow);
				logerUtil.warn("first row: " + firstRow);
			}
			if (maxRows > 0) {
				query.setMaxResults(maxRows);
				logerUtil.warn("max rows: " + maxRows);
			}

			result = query.list();
			return result;
		} catch (RuntimeException re) {
			logerUtil.error("query failed!", re);
			throw re;
		}
	}

}
