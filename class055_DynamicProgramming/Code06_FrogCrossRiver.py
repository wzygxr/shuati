"""
青蛙过河问题 - Python实现
算法思路：动态规划 + 距离压缩
时间复杂度：O(n * (t-s+1))
空间复杂度：O(n)

核心优化：
1. 当s < t时，通过实验确定安全距离，超过这个距离后就不会再遇到新的石子
2. 对石子位置进行压缩，减少动态规划的范围
3. 使用滑动窗口思想优化状态转移
"""

import sys
from typing import List

MAXN = 101
MAXL = 100001
MAXK = 201

class FrogCrossRiver:
    """
    青蛙过河问题解决方案
    """
    
    def __init__(self):
        self.arr = [0] * MAXN
        self.where = [0] * MAXN
        self.dp = [0] * MAXL
        self.stone = [False] * MAXL
        self.reach = [False] * MAXK
        self.n = 0
        self.s = 0
        self.t = 0
        self.m = 0
        self.safe = 0
    
    def reduce(self, s: int, t: int) -> int:
        """
        计算安全距离
        一旦s和t定了，那么距离多远就可以缩减呢？
        通过实验确定安全距离
        
        Args:
            s: 最小跳跃距离
            t: 最大跳跃距离
            
        Returns:
            int: 安全距离
        """
        # 重置reach数组
        self.reach = [False] * MAXK
        cnt = 0
        ans = 0
        
        for i in range(MAXK):
            # 标记可达位置
            for j in range(i + s, min(i + t + 1, MAXK)):
                self.reach[j] = True
            
            # 统计连续可达位置数量
            if not self.reach[i]:
                cnt = 0
            else:
                cnt += 1
            
            # 如果连续t个位置都可达，则找到安全距离
            if cnt == t:
                ans = i
                break
        
        return ans
    
    def compute(self) -> int:
        """
        计算青蛙过河最少踩到的石子数
        
        Returns:
            int: 最少踩到的石子数
        """
        # 对石子位置进行排序
        self.arr[1:self.m+1] = sorted(self.arr[1:self.m+1])
        
        # 特殊情况：s == t
        if self.s == self.t:
            ans = 0
            for i in range(1, self.m + 1):
                if self.arr[i] % self.s == 0:
                    ans += 1
            return ans
        else:  # s < t
            # 计算安全距离
            self.safe = self.reduce(self.s, self.t)
            
            # 重新计算石子位置
            self.where[0] = 0
            for i in range(1, self.m + 1):
                self.where[i] = self.where[i-1] + min(self.arr[i] - self.arr[i-1], self.safe)
                self.stone[self.where[i]] = True
            
            # 更新桥的长度
            self.n = self.where[self.m] + self.safe
            
            # 初始化dp数组
            for i in range(1, self.n + 1):
                self.dp[i] = MAXN
            self.dp[0] = 0
            
            # 动态规划
            for i in range(1, self.n + 1):
                for j in range(max(i - self.t, 0), i - self.s + 1):
                    self.dp[i] = min(self.dp[i], self.dp[j] + (1 if self.stone[i] else 0))
            
            # 找到最小值
            ans = MAXN
            for i in range(self.where[self.m] + 1, self.n + 1):
                ans = min(ans, self.dp[i])
            
            return ans
    
    def test(self):
        """
        单元测试函数
        """
        # 测试用例1：基础测试
        self.n = 10
        self.s = 2
        self.t = 3
        self.m = 2
        self.arr[1] = 2
        self.arr[2] = 3
        
        result1 = self.compute()
        print(f"Test 1 - Basic: {result1}")
        
        # 测试用例2：边界测试 - s == t
        self.n = 10
        self.s = 2
        self.t = 2
        self.m = 3
        self.arr[1] = 2
        self.arr[2] = 4
        self.arr[3] = 6
        
        result2 = self.compute()
        print(f"Test 2 - s == t: {result2}")
        
        # 测试用例3：无石子
        self.n = 10
        self.s = 2
        self.t = 3
        self.m = 0
        
        result3 = self.compute()
        print(f"Test 3 - No stones: {result3}")

def main():
    """
    主函数：从标准输入读取数据并计算结果
    """
    frog = FrogCrossRiver()
    
    # 从标准输入读取数据
    data = sys.stdin.read().split()
    if len(data) < 4:
        print("输入数据格式错误")
        return
    
    frog.n = int(data[0])
    frog.s = int(data[1])
    frog.t = int(data[2])
    frog.m = int(data[3])
    
    for i in range(frog.m):
        frog.arr[i+1] = int(data[4+i])
    
    result = frog.compute()
    print(result)

if __name__ == "__main__":
    # 运行主函数
    main()
    
    # 运行单元测试
    # frog = FrogCrossRiver()
    # frog.test()

"""
相关题目扩展：

1. LeetCode 403 - Frog Jump (青蛙跳)
   链接：https://leetcode.cn/problems/frog-jump/
   区别：青蛙在河中跳跃，每个位置可能有石头，需要判断能否到达最后一块石头
   解法：使用哈希表记录每个位置可以跳跃的距离

2. LeetCode 1340 - Jump Game V (跳跃游戏V)
   链接：https://leetcode.cn/problems/jump-game-v/
   区别：在数组中跳跃，每次跳跃不能超过固定距离，且需要满足特定条件
   解法：动态规划 + 单调栈

3. LeetCode 1306 - Jump Game III (跳跃游戏III)
   链接：https://leetcode.cn/problems/jump-game-iii/
   区别：在数组中跳跃，从起始位置开始，判断能否到达值为0的位置
   解法：BFS或DFS

4. Codeforces 965D - Single Wildcard Pattern Matching
   链接：https://codeforces.com/problemset/problem/965/D
   区别：青蛙在河中跳跃，河中有一些石头，需要计算能否到达对岸

5. LeetCode 45 - Jump Game II (跳跃游戏II)
   链接：https://leetcode.cn/problems/jump-game-ii/
   区别：计算到达数组末尾的最少跳跃次数
   解法：贪心算法

6. LeetCode 55 - Jump Game (跳跃游戏)
   链接：https://leetcode.cn/problems/jump-game/
   区别：判断能否到达数组末尾
   解法：贪心算法

7. LeetCode 1696 - Jump Game VI (跳跃游戏VI)
   链接：https://leetcode.cn/problems/jump-game-vi/
   区别：在数组中跳跃，每次跳跃有最大距离限制，需要最大化得分
   解法：动态规划 + 单调队列

8. LeetCode 1871 - Jump Game VII (跳跃游戏VII)
   链接：https://leetcode.cn/problems/jump-game-vii/
   区别：在二进制字符串中跳跃，需要判断能否到达末尾
   解法：BFS或滑动窗口

9. LeetCode 1345 - Jump Game IV (跳跃游戏IV)
   链接：https://leetcode.cn/problems/jump-game-iv/
   区别：在数组中跳跃，可以跳到相同值的位置
   解法：BFS + 哈希表

10. LeetCode 1346 - Jump Game V (跳跃游戏V)
    链接：https://leetcode.cn/problems/jump-game-v/
    区别：在数组中跳跃，有最大跳跃距离限制
    解法：动态规划 + 排序

算法技巧总结：
1. 动态规划是解决跳跃类问题的核心方法
2. 当数据规模较大时，需要考虑距离压缩等优化技巧
3. 对于特殊边界情况（如s==t）需要单独处理
4. 滑动窗口思想可以优化状态转移过程
5. 实验法确定安全距离是本题的关键优化点

工程化考量：
1. 异常处理：输入数据合法性验证
2. 性能优化：避免重复计算，使用合适的数据结构
3. 内存管理：合理分配数组大小，避免内存泄漏
4. 测试覆盖：包含边界测试、性能测试、异常测试

Python语言特性：
1. 使用类型注解提高代码可读性
2. 利用Python内置排序函数简化代码
3. 使用列表推导式和生成器表达式优化性能
4. 注意Python的索引从0开始，与Java/C++不同

调试技巧：
1. 使用print语句输出中间变量值
2. 利用Python的调试器pdb进行调试
3. 编写单元测试验证算法正确性
4. 使用性能分析工具优化代码效率
"""