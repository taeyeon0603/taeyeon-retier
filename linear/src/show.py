import pyodbc
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import OneHotEncoder, StandardScaler
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.metrics import mean_squared_error, r2_score


# 配置SQL Server连接
server = '47.97.207.153,8087'
database = 'Police_Basic_Info'
username = 'sa'
password = '123456'
driver = '{ODBC Driver 17 for SQL Server}'
# 建立连接
connection_string = f'DRIVER={driver};SERVER={server};DATABASE={database};UID={username};PWD={password}'
connection = pyodbc.connect(connection_string)
# 创建游标
cursor = connection.cursor()

# def createConn():
#   # 建立连接
#   connection = pyodbc.connect(connection_string)
#   return connection

# 读取数据
def read_data(sql):
    # cursor.execute(sql)
    # rows = cursor.fetchall()
    data = pd.read_sql(sql, connection)
    return data

# 写入数据
def write_data(sql):
    try:
      cursor.execute("INSERT INTO your_table (column1, column2) VALUES (?, ?)", ('value1', 'value2'))
      connection.commit()
      return True
    except Exception as e:
      connection.rollback()
      print(f"Failed to write data: {e}")
      return False

# 数据预处理
def preprocess_data(data):
    print('data', data)
    # 数据清洗
    data.dropna(inplace=True)
    # 特征工程
    # data['date'] = pd.to_datetime(data['date'], unit='ns')
    # data['activity_frequency'] = data.groupby('activity_type')['activity_type'].transform('count')
    # data['reward_count'] = data.groupby('activity_type')['reward'].transform('sum')
    # data['weekday'] = data['date'].dt.weekday
    # 将天气分类变量转换为数值
    data = pd.get_dummies(data, columns=['weather'])
    
    # 选取特征
    features = data[['last_score', 'indoor_activity_count', 'outdoor_activity_count', 'reward_count', 'vaccination_count', 'remark_count', 'visit_count', 'news_count']]
    print('features', features)
    return features

# 模型训练
def train_model(features, scores):
    print('scores', scores)
    X_train, X_test, y_train, y_test = train_test_split(features, scores, test_size=0.2, random_state=42)
    model = LinearRegression()
    model.fit(X_train, y_train)

    # 预测
    y_pred = model.predict(X_test)

    # 评估模型
    mse = mean_squared_error(y_test, y_pred)
    r2 = r2_score(y_test, y_pred)
    print(f"Mean Squared Error: {mse}")
    print(f"R-squared: {r2}")
    return model

# 实时评分函数
def calculate_score(model, feature_values):
    """
    根据提供的特征值计算评分。
    
    参数:
    feature_values (list or np.array): 一个包含特征值的数组。
    
    返回:
    float: 计算出的评分。
    """
    return model.predict([feature_values])[0]

if __name__ == "__main__":
    try:
        # 读取数据
        print("Reading data from the database:")
        data = read_data("SELECT * FROM [retire].[score]")
        # preprocess_data(data)
        # 生成示例数据 其他，当天天气，当天奖励，当天参加大型户外活动，当天参加小型户外活动，当天参加大型室内活动，当天参加小型室内活动，当天活动动态提及的次数，昨天计算活跃分数 -> 今天计算活跃分数
        # 退休人姓名、当天天气、当天奖励次数、当天参加大型户外活动次数、当天参加小型户外活动次数、当天参加大型室内活动次数、当天参加小型室内外活动次数、当天活动动态提及的次数，昨天计算活跃分数
        features = preprocess_data(data)
        # scores = np.random.rand(len(features))  # 模拟评分数据
        scores = data['score']
        model = train_model(features, scores)

        
        # 示例使用
        example_features = np.random.rand(features.shape[1])
        score = calculate_score(model, example_features)
        print(f"计算出的评分: {score}")

    except Exception as e:
        print(f"An error occurred: {e}")

    finally:
        cursor.close()
        connection.close()


# 可视化预测结果
# import matplotlib.pyplot as plt

# plt.scatter(y_test, y_pred, alpha=0.7)
# plt.xlabel('Actual Activity Score')
# plt.ylabel('Predicted Activity Score')
# plt.title('Actual vs Predicted Activity Score')
# plt.plot([y.min(), y.max()], [y.min(), y.max()], 'k--', lw=2)
# plt.show()