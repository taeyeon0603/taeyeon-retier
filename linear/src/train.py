import mysql.connector
import pandas as pd
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.metrics import mean_squared_error, r2_score
# from datetime import datetime
import joblib
import os
from dotenv import load_dotenv

load_dotenv()

# 1. 数据提取
def fetch_data():
    conn = mysql.connector.connect(
        host=os.getenv('DB_HOST'),
        port=int(os.getenv('DB_PORT')),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD'),
        database=os.getenv('DB_NAME')
    )
    
    query = """
    SELECT 
        last_score,
        indoor_activity_count,
        outdoor_activity_count,
        reward_count,
        vaccination_count,
        remark_count,
        visit_count,
        news_count,
        score
    FROM score where score IS NOT NULL
    """
    
    df = pd.read_sql(query, conn)
    conn.close()
    return df

# 2. 数据预处理
def preprocess(df):
    # 日期处理：转换为距离今天的天数
    # df['date'] = pd.to_datetime(df['date'])
    # df['days_since'] = (datetime.now() - df['date']).dt.days
    # df.drop('date', axis=1, inplace=True)
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

# 3. 模型构建与评估
def main():
    # 获取数据
    df = fetch_data()
    
    # 预处理
    X, y, numeric_features, preprocessor = preprocess(df)
    
    # 划分数据集
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42
    )
    
    # 创建管道
    pipeline = Pipeline(steps=[
        ('preprocessor', preprocessor),
        ('regressor', LinearRegression())
    ])
    
    # 训练模型
    pipeline.fit(X_train, y_train)
    # 保存模型
    model_filename = 'linear_regression_model.joblib'
    joblib.dump(pipeline, model_filename)
    print(f"模型已保存到 {model_filename}")
    
    # 预测评估
    y_pred = pipeline.predict(X_test)
    
    # 计算指标
    mse = mean_squared_error(y_test, y_pred)
    rmse = np.sqrt(mse)
    r2 = r2_score(y_test, y_pred)
    
    print(f"评估指标：")
    print(f"R²分数：{r2:.4f}")
    print(f"均方根误差(RMSE)：{rmse:.4f}")
    print(f"均方误差(MSE)：{mse:.4f}")
    
    # 输出特征重要性（标准化后的系数）
    numeric_features_processed = X_train.columns.tolist()[:len(numeric_features)]
    coefficients = pipeline.named_steps['regressor'].coef_
    feature_importance = pd.DataFrame({
        'feature': numeric_features_processed,
        'coefficient': coefficients[:len(numeric_features)]
    })
    print("\n关键特征影响系数：")
    print(feature_importance.sort_values(by='coefficient', key=abs, ascending=False))

if __name__ == "__main__":
    main()