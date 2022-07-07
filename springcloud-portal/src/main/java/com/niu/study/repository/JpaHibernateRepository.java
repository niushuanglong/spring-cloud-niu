package  com.niu.study.repository;


import com.niu.study.utils.ReflectionUtils;
import com.niu.study.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;

public abstract class JpaHibernateRepository{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @PersistenceContext
    private EntityManager em;

    /**
     * 根据jpa EntityManager 获取hibernate session
     * @return
     */
    protected Session getSession(){
        return (Session) this.em.getDelegate();
    }

    /**
     * 创建query
     * 通过params设置query查询参数
     * @param hql
     * @param params
     */
    @SuppressWarnings("rawtypes")
    protected Query createHQLQueryByParams(String hql,Object... params){
        Query query = getSession().createQuery(hql);
        if (params!=null&&params.length>0) {
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query;
    }
    /**
     * 创建query
     * 通过params设置query查询参数
     * @param <T>
     * @param hql
     * @param params
     */
    protected <T> Query<T> createHQLQueryByParams(Class<T> resultType,String hql,Object... params){
        Query<T> query = getSession().createQuery(hql,resultType);
        if (params!=null&&params.length>0) {
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query;
    }
    /**
     * 创建query
     * 通过params设置query查询参数
     * @param hql
     * @param params
     */
    @SuppressWarnings("rawtypes")
    protected Query createHQLQueryByMapParams(String hql,Map<String,Object> params){
        Query query = getSession().createQuery(hql);
        setNamedParamsOnQuery(params, query);
        return query;
    }

    /**
     * 创建query
     * 通过params设置query查询参数
     * @param hql
     * @param params
     * @param entityGraphName 对象加载图定义名词
     */
    protected javax.persistence.Query createHQLQueryByMapParamsWithEntityGraphName(String hql,Map<String,Object> params,String entityGraphName){
        javax.persistence.Query query = this.em.createQuery(hql);
        if(params!=null){
            for(String paramKey :params.keySet()){
                Object paramValue = params.get(paramKey);
                query.setParameter(paramKey, paramValue);

            }
        }
        if(StringUtils.isNotBlank(entityGraphName)){
            query.setHint("javax.persistence.loadgraph", this.em.getEntityGraph(entityGraphName)).setHint("javax.persistence.fetchgraph", this.em.getEntityGraph(entityGraphName));

        }
        return query;
    }
    /**
     * 创建query
     * 通过params设置query查询参数
     * @param <T>
     * @param hql
     * @param params
     */
    protected <T> Query<T> createHQLQueryByMapParams(Class<T> resultType,String hql,Map<String,Object> params){
        Query<T> query = getSession().createQuery(hql,resultType);
        setNamedParamsOnQuery(params, query);
        return query;
    }
    /**
     * 创建query 带分页
     * 通过params设置query查询参数
     * @param hql
     * @param params
     */
    @SuppressWarnings("rawtypes")
    protected Query createPageHQLQueryByMapParams(String hql,int pageNo,int pageSize,
                                                  Map<String,Object> params){
        Query query = getSession().createQuery(hql);
        setNamedParamsOnQuery(params, query);
        int startIndex=(pageNo-1)*pageSize;
        query.setFirstResult(startIndex).setMaxResults(pageSize);
        return query;
    }
    /**
     * 创建query 带分页
     * 通过params设置query查询参数
     * @param <T>
     * @param hql
     * @param params
     */
    protected <T> Query<T> createPageHQLQueryByMapParams(Class<T> resultType,String hql,int pageNo,int pageSize,
                                                         Map<String,Object> params){
        Query<T> query = getSession().createQuery(hql,resultType);
        setNamedParamsOnQuery(params, query);
        int startIndex=(pageNo-1)*pageSize;
        query.setFirstResult(startIndex).setMaxResults(pageSize);
        return query;
    }

    /**
     * 创建NativeQuery
     * @param sql
     * @param params 参数为数组
     */
    @SuppressWarnings("rawtypes")
    protected NativeQuery createSQLQueryByParams(String sql,Object... params){
        NativeQuery query = getSession().createNativeQuery(sql);
        if (params!=null&&params.length>0) {
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query;
    }
    /**
     * 创建NativeQuery
     * @param <T>
     * @param sql
     * @param params 参数为数组
     */
    protected <T> NativeQuery<T> createSQLQueryByParams(Class<T> resultType,String sql,Object... params){
        NativeQuery<T> query = getSession().createNativeQuery(sql,resultType);
        if (params!=null&&params.length>0) {
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query;
    }
    /**
     * 创建NativeQuery
     * @param sql
     * @param params
     */
    @SuppressWarnings("rawtypes")
    protected NativeQuery createSQLQueryByMapParams(String sql,Map<String,Object> params){
        NativeQuery query = getSession().createNativeQuery(sql);
        setNamedParamsOnQuery(params, query);
        return query;
    }
    /**
     * 创建NativeQuery
     * @param <T>
     * @param sql
     * @param params
     */
    protected <T> NativeQuery<T> createSQLQueryByMapParams(Class<T> resultType,String sql,Map<String,Object> params){
        NativeQuery<T> query = getSession().createNativeQuery(sql,resultType);
        setNamedParamsOnQuery(params, query);
        return query;
    }
    /**
     * 创建NativeQuery 带分页
     * @param pageNo
     * @param params
     */
    @SuppressWarnings("rawtypes")
    protected NativeQuery createPageSQLQueryByMapParams(String sql,int pageNo,int pageSize,
                                                        Map<String,Object> params){
        NativeQuery query = getSession().createNativeQuery(sql);
        setNamedParamsOnQuery(params, query);
        int startIndex=(pageNo-1)*pageSize;
        query.setFirstResult(startIndex).setMaxResults(pageSize);
        return query;
    }
    /**
     * 创建NativeQuery 带分页
     * @param <T>
     * @param sql
     * @param params
     */
    protected <T> NativeQuery<T> createPageSQLQueryByMapParams(Class<T> resultType,String sql,int pageNo,int pageSize,
                                                               Map<String,Object> params){
        NativeQuery<T> query = getSession().createNativeQuery(sql,resultType);
        setNamedParamsOnQuery(params, query);
        int startIndex=(pageNo-1)*pageSize;
        query.setFirstResult(startIndex).setMaxResults(pageSize);
        return query;
    }

    @SuppressWarnings("rawtypes")
    private void setNamedParamsOnQuery(Map<String, Object> params, Query query) {
        if (params!=null&&params.size()>0) {
            for(Map.Entry<String,Object> entry:params.entrySet()){
                Object paramKey = entry.getKey();
                Object paramValue = entry.getValue();
                if(paramValue instanceof List){
                    query.setParameterList((String)paramKey, (List)paramValue);
                }else if(paramValue instanceof String[]){
                    query.setParameterList((String)paramKey, (String[])paramValue);
                }else if(paramValue instanceof Object[]){
                    query.setParameterList((String)paramKey, (Object[])paramValue);
                }else if(paramValue instanceof Collection){
                    query.setParameterList((String)paramKey, (Collection)paramValue);
                }else{
                    query.setParameter((String)paramKey, paramValue);
                }
            }
        }
    }

    public int batchUpdateSql(final String sql,final List<Object[]> batchParams){
        long start = System.currentTimeMillis();
        final BatchUpdatedResult updateResult = new BatchUpdatedResult();
        this.getSession().doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                PreparedStatement ps = null ;
                try{
                    ps = conn.prepareStatement(sql);
                    for(Object[] params : batchParams){
                        if(params == null){
                            continue;
                        }
                        for(int i=0;i<params.length;i++){
                            Object p = params[i];
                            if(p == null){
                                ps.setNull(i+1,Types.VARCHAR);
                            }
                            else if( p instanceof String){
                                String pstr = (String)p;
                                ps.setString(i+1, pstr);
                            }else if(p instanceof Integer){
                                int n = (Integer)p;
                                ps.setInt(i+1, n);
                            }else if(p instanceof Date){
                                Date d = (Date)p;
                                ps.setTimestamp(i+1, new Timestamp(d.getTime()));
                            }else if(p instanceof Long){
                                Long d = (Long)p;
                                ps.setLong(i+1, d);
                            }else if(p instanceof Byte){
                                Byte d = (Byte)p;
                                ps.setByte(i+1, d);
                            }else if(p instanceof Boolean){
                                Boolean d = (Boolean)p;
                                ps.setBoolean(i+1, d);
                            }else if(p instanceof Double){
                                Double d = (Double)p;
                                ps.setDouble(i+1, d);
                            }else if(p instanceof BigDecimal){
                                BigDecimal d = (BigDecimal)p;
                                ps.setBigDecimal(i+1, d);
                            }else if(p instanceof Float){
                                Float d = (Float)p;
                                ps.setFloat(i+1, d);
                            }
                            else{
                                throw new RuntimeException("未识别的参数类型:"+p.getClass());
                            }
                        }

                        ps.addBatch();
                    }
                    int[] updateds = ps.executeBatch();
                    for(int update:updateds) {
                        updateResult.addUpdated(update);
                    }
                }finally{
                    if(ps!=null){
                        ps.close();
                    }
                }
            }
        });
        logger.info("耗时["+(System.currentTimeMillis()-start)+"]毫秒批量执行SQL:"+sql+"");
        return updateResult.updated;
    }

    public int batchUpdateSql(final String sql,final List<Object[]> batchParams,int[] sqlTypes){
        long start = System.currentTimeMillis();
        final BatchUpdatedResult updateResult = new BatchUpdatedResult();
        this.getSession().doWork(new Work() {

            @Override
            public void execute(Connection conn) throws SQLException {
                PreparedStatement ps = null ;
                try{
                    ps = conn.prepareStatement(sql);
                    for(Object[] params : batchParams){
                        if(params == null){
                            continue;
                        }
                        if(sqlTypes==null || sqlTypes.length!=params.length){
                            throw new IllegalArgumentException("sqlTypes.length must be equal to params.length");
                        }
                        for(int i=0;i<params.length;i++){
                            Object p = params[i];

                            if(p == null){
                                ps.setNull(i+1,sqlTypes[i]);
                            }
                            else if( p instanceof String){
                                String pstr = (String)p;
                                ps.setString(i+1, pstr);
                            }else if(p instanceof Integer){
                                int n = (Integer)p;
                                ps.setInt(i+1, n);
                            }else if(p instanceof Date){
                                Date d = (Date)p;
                                ps.setTimestamp(i+1, new Timestamp(d.getTime()));
                            }else if(p instanceof Long){
                                Long d = (Long)p;
                                ps.setLong(i+1, d);
                            }else if(p instanceof Byte){
                                Byte d = (Byte)p;
                                ps.setByte(i+1, d);
                            }else if(p instanceof Boolean){
                                Boolean d = (Boolean)p;
                                ps.setBoolean(i+1, d);
                            }else if(p instanceof Double){
                                Double d = (Double)p;
                                ps.setDouble(i+1, d);
                            }else if(p instanceof BigDecimal){
                                BigDecimal d = (BigDecimal)p;
                                ps.setBigDecimal(i+1, d);
                            }else if(p instanceof Float){
                                Float d = (Float)p;
                                ps.setFloat(i+1, d);
                            }
                            else{
                                throw new RuntimeException("未识别的参数类型:"+p.getClass());
                            }
                        }

                        ps.addBatch();
                    }
                    int[] updateds = ps.executeBatch();
                    for(int update:updateds) {
                        updateResult.addUpdated(update);
                    }
                }finally{
                    if(ps!=null){
                        ps.close();
                    }
                }
            }
        });
        logger.info("耗时["+(System.currentTimeMillis()-start)+"]毫秒批量执行SQL:"+sql+"");
        return updateResult.updated;
    }

    static class BatchUpdatedResult{
        int updated = 0;
        void addUpdated(int u) {
            this.updated += u;
        }
    }

    /**
     * 根据Ids查找entity列表
     * @param entityClass
     * @param orderedIds
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByOrderedIds(Class<T> entityClass,Collection<String> orderedIds){
        List<T> result = new ArrayList<T>();
        if(orderedIds != null && orderedIds.size() > 0 ){
            List<List<String>> splittedIdsList = Utils.splitToFixedList(orderedIds, 500);
            Map<String, T> entityMap = new HashMap<String, T>();
            for(List<String> ids : splittedIdsList){
                List<T> entities = this.createHQLQueryByMapParams(entityClass,
                        "select o from "+entityClass.getName()+" o where o.id in (:ids)",
                        Utils.buildMap("ids",ids)).list();
                for(T t : entities){
                    String id =(String) ReflectionUtils.getProperty(t, "id");
                    entityMap.put(id, t);
                }
            }
            for(String id : orderedIds){
                if(entityMap.containsKey(id)){
                    result.add(entityMap.get(id));
                }
            }
        }

        return result;
    }
}