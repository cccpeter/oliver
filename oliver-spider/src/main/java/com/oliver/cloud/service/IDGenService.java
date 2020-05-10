package com.oliver.cloud.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.oliver.cloud.dao.RedisDao;
import com.oliver.cloud.entity.IdEntity;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.PropertyFactory;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.dao.IDAllocDao;
import com.sankuai.inf.leaf.segment.dao.impl.IDAllocDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.logging.Logger;


@Service
/**
 * @Author: cpeter
 * @Desc: 生成ID的service类
 * @Date: cretead in 2019/10/17 18:00
 * @Last Modified: by
 * @return value
 */
public class IDGenService {

    @Autowired
    private RedisDao redisDao;
    private final int IDLENGTH = 2000;

    private Logger logger = Logger.getLogger("IDGenService");
/**
 * @Author: cpeter
 * @Desc: 使用步长算法生成ID
 * @Date: cretead in 2019/10/17 17:59
 * @Last Modified: by
 * @return ID
 */
    public IdEntity getCacheId(){
        String getId = redisDao.rpop("ID");
       if(getId == null){
           IDGen idGen;
           DruidDataSource dataSource;
           Properties properties = PropertyFactory.getProperties();
           // Config dataSource
           dataSource = new DruidDataSource();
           dataSource.setUrl(properties.getProperty("jdbc.url"));
           dataSource.setUsername(properties.getProperty("jdbc.username"));
           dataSource.setPassword(properties.getProperty("jdbc.password"));
           try {
               dataSource.init();
           }catch (Exception e){
               logger.warning("初始化数据库失败，请检查账号密码");
           }
           // Config Dao
           IDAllocDao dao = new IDAllocDaoImpl(dataSource);
           // Config ID Gen
           idGen = new SegmentIDGenImpl();
           ((SegmentIDGenImpl) idGen).setDao(dao);
           idGen.init();
           for (int i = 0; i < IDLENGTH; ++i) {
               com.sankuai.inf.leaf.common.Result r = idGen.get("leaf-segment-test");
               redisDao.lpush("ID",String.valueOf(r.getId()));
           }
           getId = redisDao.rpop("ID");
       }
       IdEntity idEntity = new IdEntity();
       idEntity.setId(getId);
       return idEntity;
    }
}
