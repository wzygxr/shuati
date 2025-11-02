import random

"""
Fisher-Yates 洗牌算法
算法思想：从数组末尾开始，将当前位置与之前的随机位置交换，确保每个元素都有相同的概率出现在任意位置
时间复杂度：O(n)
空间复杂度：O(1) - 原地洗牌

相关题目：
1. LeetCode 384. 打乱数组 - https://leetcode-cn.com/problems/shuffle-an-array/
2. LintCode 1423. 随机洗牌 - https://www.lintcode.com/problem/1423/
3. CodeChef - SHUFFLE - https://www.codechef.com/problems/SHUFFLE
"""

def fisher_yates_shuffle(arr):
    """
    Fisher-Yates 洗牌算法实现
    
    Args:
        arr: 需要洗牌的列表
    """
    if not arr or len(arr) <= 1:
        return
    
    # 从后往前遍历列表
    for i in range(len(arr) - 1, 0, -1):
        # 生成 [0, i] 范围内的随机索引
        j = random.randint(0, i)
        # 交换 arr[i] 和 arr[j]
        arr[i], arr[j] = arr[j], arr[i]

# 测试函数
def test_shuffle():
    arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("原始数组：")
    print(arr)
    
    fisher_yates_shuffle(arr)
    
    print("洗牌后数组：")
    print(arr)

if __name__ == "__main__":
    test_shuffle()