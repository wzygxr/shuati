# 威佐夫博弈(Wythoff Game)
# 有两堆石子，数量任意，可以不同，游戏开始由两个人轮流取石子
# 游戏规定，每次有两种不同的取法
# 1) 在任意的一堆中取走任意多的石子
# 2) 可以在两堆中同时取走相同数量的石子
# 最后把石子全部取完者为胜者
# 现在给出初始的两堆石子的数目，返回先手能不能获胜
# 测试链接 : https://www.luogu.com.cn/problem/P2252
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下的code，提交时请把类名改成"Main"，可以直接通过
#
# 算法思路：
# 1. 威佐夫博弈是基于黄金分割率的博弈问题
# 2. 核心理论：奇异局势(必败态)满足 ak = floor(k * (sqrt(5)+1) / 2), bk = ak + k
# 3. 当两堆石子数满足这个关系时，先手必败；否则先手必胜
# 4. 为了处理大数和高精度问题，使用高精度计算
#
# 时间复杂度：O(1) - 只需要进行常数次数学运算
# 空间复杂度：O(1) - 只使用了常数级别的额外空间
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 两堆石子
#    - 两人轮流取石子
#    - 可从一堆取任意数量或从两堆取相同数量石子
#    - 取走最后一颗石子者获胜
# 2. 解题技巧：
#    - 判断是否为奇异局势（必败态）
#    - 利用黄金分割率进行计算
#    - 注意处理大数和精度问题
# 3. 变种问题：
#    - 不同的取石子规则
#    - 最后取石子者失败
#
# 相关题目链接：
# 1. 洛谷 P2252: https://www.luogu.com.cn/problem/P2252
# 2. POJ 1067: http://poj.org/problem?id=1067
# 3. HDU 1527: http://acm.hdu.edu.cn/showproblem.php?pid=1527

import sys
import threading
import math

# 黄金分割比例
# 为了处理大数和高精度问题，使用较高精度的黄金分割率
split = (1 + math.sqrt(5)) / 2

def compute(a, b):
    """
    计算威佐夫博弈结果
    :param a: 第一堆石子数
    :param b: 第二堆石子数
    :return: 1表示先手胜，0表示先手败
    
    算法思路：
    1. 计算两堆石子数的差值
    2. 差值乘以黄金分割率，向下取整
    3. 如果结果等于较小的堆数，则先手必败；否则先手必胜
    
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    min_val = min(a, b)
    max_val = max(a, b)
    # 威佐夫博弈
    # 小 != (大 - 小) * 黄金分割比例，先手赢
    # 小 == (大 - 小) * 黄金分割比例，后手赢
    # 要向下取整
    result = int((max_val - min_val) * split)
    if min_val != result:
        return 1
    else:
        return 0

def main():
    # 读取输入并处理
    try:
        while True:
            line = input().strip()
            if not line:
                break
            values = list(map(int, line.split()))
            a, b = values[0], values[1]
            print(compute(a, b))
    except EOFError:
        pass

# 为了提高输入输出效率，使用以下方式运行
if __name__ == "__main__":
    sys.setrecursionlimit(1 << 25)
    threading.stack_size(1 << 27)
    thread = threading.Thread(target=main)
    thread.start()
    thread.join()