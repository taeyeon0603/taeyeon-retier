import mysql.connector
import pandas as pd
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.metrics import mean_squared_error, r2_score
from datetime import datetime
from dateutil.relativedelta import relativedelta
from collections import Counter
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker
import joblib
import os
from dotenv import load_dotenv

load_dotenv()

# 1. 数据提取
def fetch_data(query):
    conn = mysql.connector.connect(
        host=os.getenv('DB_HOST'),
        port=int(os.getenv('DB_PORT')),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD'),
        database=os.getenv('DB_NAME')
    )
    
    df = pd.read_sql(query, conn)
    conn.close()
    return df
# 2. 数据预处理
def preprocess(df):
    # 分离特征和目标变量
    X = df.drop('score', axis=1)
    y = df['score']
    
    # 定义预处理管道
    numeric_features = [
        'last_score', 
        'indoor_activity_count',
        'outdoor_activity_count',
        'reward_count',
        'vaccination_count',
        'remark_count',
        'visit_count',
        'news_count',
    ]
    # categorical_features = ['weather']
    preprocessor = ColumnTransformer(
        transformers=[
            ('num', StandardScaler(), numeric_features),
        ])
    
    return X, y, numeric_features, preprocessor

def counter(titles):
    user_names = [log.split('参加活动')[0] for log in titles]
    # 使用 Counter 统计每个用户名的出现次数
    user_counter = Counter(user_names)
    return user_counter

def main():
    # 有效用户列表
    users = fetch_data("""
    SELECT id,id as retire_id,name FROM retire_info where is_del = 0
    """)
    print(users, f"Type of int_var: {type(users)}")
    # 今日每个用户户外运动次数  取当天日期
    today = datetime.today()
    event_date = today.strftime('%Y-%m-%d') #'2025-02-20'
    event_date_obj = datetime.strptime(event_date, '%Y-%m-%d')
    # 减去一个月，将新的 datetime 对象转换回字符串
    new_event_date_obj = event_date_obj - relativedelta(months=1)
    event_date_start = new_event_date_obj.strftime('%Y-%m-%d')
    new_event_date_obj = event_date_obj + relativedelta(days=1)
    event_date_end = new_event_date_obj.strftime('%Y-%m-%d')
    addr_pattern = '%户外%'
    retire_ids = users['id']
    retire_ids_str = ', '.join(f"'{id}'" for id in retire_ids)

    outdoor_activity_df = fetch_data(f"""
    SELECT
      retire_id,
      COUNT(1) as outdoor_activity_count
    FROM
      activities 
    WHERE 
      DATE(event_date) = '{event_date}' 
      AND addr LIKE '{addr_pattern}' 
      AND retire_id IN ({retire_ids_str}) GROUP BY retire_id
    """)
    print('indoor_activitys', outdoor_activity_df)
    # 今日每个用户室内运动次数
    indoor_activity_df = fetch_data(f"""
    SELECT
      retire_id,
      COUNT(1) as indoor_activity_count
    FROM
      activities 
    WHERE 
      DATE(event_date) = '{event_date}' 
      AND addr NOT LIKE '{addr_pattern}' 
      AND retire_id IN ({retire_ids_str}) GROUP BY retire_id
    """)
    print('outdoor_activitys', indoor_activity_df)
    # 最近一个月内每个用户获得奖励次数
    reward_df = fetch_data(f"""
    SELECT
      retire_id,
      COUNT(1) AS reward_count 
    FROM
      reward 
    WHERE
      reward_time BETWEEN '{event_date_start}' AND '{event_date_end}'
      AND retire_id IN ({retire_ids_str}) GROUP BY retire_id
    """)
    print('reward', reward_df)
    # 最近一个月内每个用户的接种疫苗数
    vaccination_df = fetch_data(f"""
    SELECT
      retire_id,
      COUNT(1) AS vaccination_count 
    FROM
      vaccination 
    WHERE
      inoculate_at BETWEEN '{event_date_start}' AND '{event_date_end}'
      AND retire_id IN ({retire_ids_str}) GROUP BY retire_id
    """)
    print('vaccination', vaccination_df)
    # 最近一个月内每个用户的联系人数
    remark_df = fetch_data(f"""
    SELECT
      retire_id,
      COUNT(1) AS remark_count 
    FROM
      remarks 
    WHERE
      event_date BETWEEN '{event_date_start}' AND '{event_date_end}'
      AND retire_id IN ({retire_ids_str}) GROUP BY retire_id
    """)
    print('remarks', remark_df)
    # 最近一个月内每个用户的访问次数
    visit_df = fetch_data(f"""
    SELECT
      retire_id,
      COUNT(1) AS visit_count 
    FROM
      visit_record 
    WHERE
      visit_date BETWEEN '{event_date_start}' AND '{event_date_end}'
      AND retire_id IN ({retire_ids_str}) GROUP BY retire_id
    """)
    print('visit_record', visit_df)
    # 最近一个月内每个用户的活动动态次数
    work_news_df = fetch_data(f"""
    SELECT
      title
    FROM
      work_news 
    WHERE
      is_deleted = 0 
      AND create_at BETWEEN '{event_date_start}' AND '{event_date_end}'
    """)
    print('work_news', work_news_df)
    news_counts = counter(work_news_df['title'])
    print('news_counts', news_counts, f"Type of int_var: {type(news_counts)}")
    news_counts_df = pd.DataFrame(list(news_counts.items()), columns=['name', 'news_count'])
    news_counts_df.set_index('name', inplace=True)
    print('news_counts_df', news_counts_df)

    # 上一次分数
    score_df = fetch_data(f"""
    SELECT s1.retire_id, s1.score AS last_score
    FROM score s1
    JOIN (
        SELECT retire_id, MAX(id) AS max_id
        FROM score
        GROUP BY retire_id
    ) s2 ON s1.id = s2.max_id AND s1.retire_id = s2.retire_id
    """)
    print('score', score_df)
    users.set_index('id', inplace=True)
    users = users.merge(outdoor_activity_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(indoor_activity_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(reward_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(vaccination_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(remark_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(visit_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(score_df.set_index('retire_id'), left_index=True, right_index=True, how='left').fillna(0)
    users = users.merge(news_counts_df, left_on='name', right_index=True, how='left').fillna(0)
    users.drop(columns=['name'], inplace=True)
    current_date = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    users['date'] = current_date
    # users['retire_id'] = users['id']
    users['weather'] = 'sunny'

    max_df = fetch_data(f"""
    SELECT
      MAX(id) AS id 
    FROM
      score
    """)
    maxid = max_df.iloc[0]['id']
    print('maxid', maxid)
    users['id'] = range(maxid + 1, len(users) + maxid + 1)
    # selected_df = users[['retire_id', 'last_score']]
    print('users', users)
    
    # 加载模型，算出它的新分数
    model_filename = 'linear_regression_model.joblib'
    loaded_pipeline = joblib.load(model_filename)
    print(f"模型已从 {model_filename} 加载")
    # 预测评估
    y_pred = loaded_pipeline.predict(users)
    # 四舍五入取整并确保值在 1 到 100 之间
    score_array = np.array(y_pred)
    rounded_scores = np.round(score_array).astype(int)
    clipped_scores = np.clip(rounded_scores, 1, 100)
    users['score'] = clipped_scores
    print('y_pred', users)

    engine = create_engine(f"mysql+mysqlconnector://{os.getenv('DB_USER')}:{os.getenv('DB_PASSWORD')}@{os.getenv('DB_HOST')}:{os.getenv('DB_PORT')}/{os.getenv('DB_NAME')}")
    today = datetime.now().strftime('%Y-%m-%d')
    del_query = f"""
    DELETE FROM score
    WHERE date >= '{today} 00:00:00' AND date < '{today} 23:59:59'
    """
    with engine.connect() as enconn:
      enconn.execute(text(del_query))
      enconn.commit()

    # 将 DataFrame 插入到 MySQL
    users.to_sql('score', con=engine, if_exists='append', index=False)
    print("Data inserted successfully.")

    # 根据分数更新用户的active_rate
    users['active_rate'] = (users['score'] // 20) + 1
    users['active_rate'] = users['active_rate'].clip(upper=5)
    print('active_rate', users[['retire_id', 'score', 'active_rate']])
    # 创建会话
    Session = sessionmaker(bind=engine)
    session = Session()
    # 更新 retire_info 表
    for index, row in users.iterrows():
        query_update = f"""
        UPDATE retire_info
        SET active_rate = {row['active_rate']},score={row['score']}
        WHERE id = '{row['retire_id']}'
        """
        session.execute(text(query_update))

    # 提交事务 # 关闭会话
    session.commit()
    session.close()


if __name__ == "__main__":
    main()