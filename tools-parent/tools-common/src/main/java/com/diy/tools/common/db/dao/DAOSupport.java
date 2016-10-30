package com.diy.tools.common.db.dao;


import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface DAOSupport {

	public abstract Serializable insert(Object obj);

	public abstract void update(Object obj);

	public abstract int update(String s, Object aobj[]);

	public abstract void update(List list);

	public abstract void delete(Object obj);

	public abstract void delete(Class class1, Serializable serializable);

	public abstract void delete(List list);

	public void executeHql(String hql, List value);

	public abstract List createQuery(String s, List list, int i, int j);

	public abstract List createSQLQuery(String s, List list, int i, int j);

	public abstract Object load(Class class1, Serializable serializable);

	public abstract Object loadAll(Class c);

	public abstract Object getObjectById(Class class1, Serializable serializable);

	public abstract int execute(String s, List list);

	public List querySQL(String sql, Object[] values, int firstRow, int maxRows);

	public abstract Session getHbSession();

	public abstract Session getSpSession();
}
