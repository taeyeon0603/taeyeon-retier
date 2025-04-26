import schedule
import time
import datetime
# import os
import subprocess
from datetime import datetime, timedelta
# 定时更新
def job():
    print(f"任务执行时间: {datetime.datetime.now()}")
    # 在这里编写你需要执行的任务
    # 例如，调用某个函数或执行某个操作
    perform_task()

def perform_task():
    print("执行具体任务")

# 定义执行 train.py 的函数
def run_train():
    now = datetime.now()
    # 每月1号01:00点运行训练
    if (now.day == 1):
        print(f"执行 train.py 任务时间: {datetime.now()}")
        subprocess.run(["python", "src/train.py"])

# 定义执行 calc.py 的函数
def run_calc():
    print(f"执行 calc.py 任务时间: {datetime.now()}")
    subprocess.run(["python", "src/calc.py"])

# 安排每天 23:00 执行 calc.py
schedule.every().day.at("23:00").do(run_calc)

# 安排每个月月初执行 train.py
schedule.every().day.at("01:00").do(run_train)

if __name__ == "__main__":
    # 安排每天 23:00 执行任务
    # schedule.every().day.at("21:11").do(job)
    # 初始化时执行一次 train.py
    print('task ===>')
    run_train()
    # 演示，实际不需要立即运行
    # run_calc()  

    while True:
        # 运行所有已安排的任务
        schedule.run_pending()
        # 每分钟检查一次是否有任务需要执行
        time.sleep(60)