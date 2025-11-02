# 种花问题
# 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
# 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
# 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，
# 其中 0 表示没种植花，1 表示种植了花。
# 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
# 能则返回 true ，不能则返回 false 。
# 测试链接 : https://leetcode.cn/problems/can-place-flowers/

def canPlaceFlowers(flowerbed, n):
    """
    种花问题
    
    算法思路：
    使用贪心策略：
    1. 遍历花坛数组
    2. 对于每个位置，检查是否可以种花：
       - 当前位置为0
       - 前一个位置为0或不存在
       - 后一个位置为0或不存在
    3. 如果可以种花，就种下并计数
    4. 最后比较种花数量与n的大小
    
    正确性分析：
    1. 贪心选择：能种就种，这样可以种下最多的花
    2. 局部最优解能够达到全局最优解
    3. 种花后将位置标记为1，避免重复计算
    
    时间复杂度：O(n) - 只需要遍历数组一次
    空间复杂度：O(1) - 只使用常数额外空间
    
    :param flowerbed: 花坛数组
    :param n: 需要种的花的数量
    :return: 是否能种下n朵花
    """
    count = 0  # 已种花的数量
    
    # 遍历花坛数组
    for i in range(len(flowerbed)):
        # 检查当前位置是否可以种花
        if flowerbed[i] == 0:
            # 检查前一个位置是否为0或不存在
            prevEmpty = (i == 0) or (flowerbed[i - 1] == 0)
            # 检查后一个位置是否为0或不存在
            nextEmpty = (i == len(flowerbed) - 1) or (flowerbed[i + 1] == 0)
            
            # 如果前后都为空，可以种花
            if prevEmpty and nextEmpty:
                flowerbed[i] = 1  # 种花
                count += 1        # 计数增加
                
                # 如果已经种够了，提前返回
                if count >= n:
                    return True
    
    # 返回是否能种下n朵花
    return count >= n

# 测试用例
if __name__ == "__main__":
    # 测试用例1: flowerbed = [1,0,0,0,1], n = 1 -> 输出: true
    flowerbed1 = [1, 0, 0, 0, 1]
    n1 = 1
    print("测试用例1:")
    print("花坛:", flowerbed1, ", n =", n1)
    print("能否种下:", canPlaceFlowers(flowerbed1.copy(), n1))  # 期望输出: True
    
    # 测试用例2: flowerbed = [1,0,0,0,1], n = 2 -> 输出: false
    flowerbed2 = [1, 0, 0, 0, 1]
    n2 = 2
    print("\n测试用例2:")
    print("花坛:", flowerbed2, ", n =", n2)
    print("能否种下:", canPlaceFlowers(flowerbed2.copy(), n2))  # 期望输出: False
    
    # 测试用例3: flowerbed = [0,0,1,0,0], n = 1 -> 输出: true
    flowerbed3 = [0, 0, 1, 0, 0]
    n3 = 1
    print("\n测试用例3:")
    print("花坛:", flowerbed3, ", n =", n3)
    print("能否种下:", canPlaceFlowers(flowerbed3.copy(), n3))  # 期望输出: True
    
    # 测试用例4: flowerbed = [0], n = 1 -> 输出: true
    flowerbed4 = [0]
    n4 = 1
    print("\n测试用例4:")
    print("花坛:", flowerbed4, ", n =", n4)
    print("能否种下:", canPlaceFlowers(flowerbed4.copy(), n4))  # 期望输出: True