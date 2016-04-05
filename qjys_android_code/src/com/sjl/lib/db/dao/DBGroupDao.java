package com.sjl.lib.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.sjl.lib.db.DatabaseHelper;

public class DBGroupDao {
	private Context context;
	private Dao<DBGroup, Integer> userDaoOpe;
	private DatabaseHelper helper;

	public DBGroupDao(Context context) {
		this.context = context;
		try {
			helper = DatabaseHelper.getHelper(context);
			userDaoOpe = helper.getDao(DBGroup.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加一个用户
	 * 
	 * @param user
	 */
	public void add(DBGroup user) {
		try {
			userDaoOpe.create(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// ...other operations

	public void testAddDBGroup() {

		/*// (1)主键:
		// @DatabaseField(id=true)
		// @DatabaseField(generatedId = true) ，generatedId 表示id为主键且自动生成
		// (2)列名: 不指定的话就是和变量名一样的
		// @DatabaseField(columnName="columnName")
		// (3) 数据类型: 这个一般情况下都不用指定，可以根据java 类获得
		// @DatabaseField(dataType=DataType.INTEGER)
		// (4) 默认值:
		// @DatabaseField(defaultValue="0")
		// (5)长度:一般用于String型
		// @DatabaseField(width=13)
		// (6) 能否为空:默认为True
		// @DatabaseField(canBeNull=false)
//	    @DatabaseField(canBeNull = true, foreign = true, columnName = "user_id")  
//	    private User user; 
 * 
		cloumnName:指定字段名,不指定则变量名作为字段名	 canBeNull:是否可以为null	 
		dataType:指定字段的类型	defaultValue:指定默认值	 width:指定长度
		 id:指定字段为id	generatedId:指定字段为自增长的id,不能id,generatedIdSequence通用
		foreign 指定这个字段的对象是一个外键,外键值是这个对象的id
		useGetSet:指定ormlite访问变量使用set,get方法默认使用的是反射机制直接访问变量	throwIfNull,如果空值抛出异常	persisted:指定是否持久化此变量,默认true
		unique:字段值唯一	uniqueCombo整列的值唯一	index:索引
		uniqueIndex 唯一索引	foreignAutoRefresh 外键值,自动刷新	foreignAutoCreate 外键不存在时是否自动添加到外间表中
		foreignColumnName外键字段指定的外键表中的哪个字段	 	 
		                 @ForeignCollectionField  表示这个表中的数据在其他表中是外键(其他表的某个字段使用@DatabaseField(foreignColumnName=一个表的id键名,foreign=true)

		                                          eager 表示该集合是在初始化这个对象的时候,是否讲对象取出还是在遍历的时候才取出,默认false遍历的时候才取出,size()方法也会引起遍历
		
		主要方法:

            create:插入一条数据

            createIfNotExists:如果不存在则插入

            createOrUpdate:如果指定id则更新

            queryForId:更具id查找

            update 查找出数据

            refresh的解释:If you want to use other elds in the Account, you must call refresh on the accountDao class to get the Account object lled in.

            delte 删除数据

            queryBuilder() 创建一个查询生成器:进行复杂查询

           deleteBuilder() 创建一个删除生成器,进程复杂条件删除

           updateBuilder() 创建修条件生成器,进行复杂条件修改

条件查找器DeleteBuilder,QueryBuilder,UpdateBuilder

           查找器是帮助拼接条件语句的.比如查找器中有 where()方法 and()方法 eq()方法 lt()方法 qt()方法 between方法这些方法很直观..很容易都明了什么意思

           最后使用prepare()方法生成条件使用DAO.query || DAO.delete|| DAO.update 方法执行

          可以使用查找生成器QueryBuilder 的 orderby limit offset 方法进行排序,分页
          使用select语句查询
          rderDao.queryRaw(  
         "select count(*) from orders where account_id = 10"); 
         
         Map<String, Object> userMap=new HashMap<String, Object>();  
        userMap.put("username", username);  
        userMap.put("password", password);  
         List<UsersEntity> userlistEntities=userDao.queryForFieldValues(userMap);  
         
         
         return articleDaoOpe.queryBuilder().where().eq("user_id", userId)  
                    .query();  
             	QueryBuilder<Article, Integer> queryBuilder = articleDaoOpe
					.queryBuilder();
			Where<Article, Integer> where = queryBuilder.where();
			where.eq("user_id", 1);
			where.and();
			where.eq("name", "xxx");

			//或者
			articleDaoOpe.queryBuilder().//
					where().//
					eq("user_id", 1).and().//
					eq("name", "xxx");       
                    
                    
        根据文章id 更新user对象
                    article = articleDaoOpe.queryForId(id);  
            helper.getDao(User.class).refresh(article.getUser());  或@DatabaseField(canBeNull = true, foreign = true, columnName = "user_id", foreignAutoRefresh = true)
            
            
            关联一个集合
每个User关联一个或多个Article，如果我在User中声明一个Collection<Article> articles，我能否在查询User的时候，一并能够获取到articles的值呢？

答案是可以的。在User中添加如下属性，且注解如下：

@ForeignCollectionField
private Collection<Article> articles;


//事务操作
		TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});
	／／join
				QueryBuilder<Article, Integer> articleBuilder = articleDaoOpe
					.queryBuilder();
			QueryBuilder userBuilder = helper.getDao(User.class).queryBuilder();
			articleBuilder.join(userBuilder);
			
			
			同理还有deleteBuilder还是建议直接拼写sql，当然很简单的除外，直接使用它的API~

          *
          */

	}

	public static void testAddDBGroup(Context contect) {

		DBGroup u1 = new DBGroup("zhy", "2B青年");
		DatabaseHelper helper = DatabaseHelper.getHelper(contect);
		try {
			helper.getDao(DBGroup.class).create(u1);
			u1 = new DBGroup("zhy2", "2B青年");
			helper.getDao(DBGroup.class).createIfNotExists(u1);
			u1 = new DBGroup("zhy3", "2B青年");
			helper.getDao(DBGroup.class).create(u1);
			u1 = new DBGroup("zhy4", "2B青年");
			helper.getDao(DBGroup.class).create(u1);
			u1 = new DBGroup("zhy5", "2B青年");
			helper.getDao(DBGroup.class).create(u1);
			u1 = new DBGroup("zhy6", "2B青年");
			helper.getDao(DBGroup.class).create(u1);

			testList(contect);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void queryDBGroup(Context contect) {
		
		DatabaseHelper helper = DatabaseHelper.getHelper(contect);
		try {
			List list = helper.getDao(DBGroup.class).queryForAll();
			
			Log.d("sssss","list.size()="+list.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void testDeleteUser(Context contect) {
		DatabaseHelper helper = DatabaseHelper.getHelper(contect);
	}

	public void testUpdateUser(Context contect) {
		DatabaseHelper helper = DatabaseHelper.getHelper(contect);
		try {
			DBGroup u1 = new DBGroup("zhy-android", "2B青年");
			helper.getDao(DBGroup.class).update(u1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void testList(Context contect) {
		DatabaseHelper helper = DatabaseHelper.getHelper(contect);
		try {
			DBGroup u1 = new DBGroup("zhy-android", "2B青年");
			//List<User> users = helper.getDao(DBGroup.class).queryForAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
