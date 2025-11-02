# 统计数字1的个数
# 给定一个整数n，计算所有小于等于n的非负整数中数字1出现的个数
# 测试链接 : https://leetcode.cn/problems/number-of-digit-one/


class Solution:
    def countDigitOne(self, n: int) -> int:
        """
        数位DP方法
        时间复杂度: O(log n) 每个数位最多计算两次(受限/不受限)
        空间复杂度: O(log n) 递归栈深度
        """
        if n <= 0:
            return 0
        
        # 将数字n转换为字符串，方便按位处理
        s = str(n)
        length = len(s)
        
        # dp[i][count][is_limit] 表示处理到第i位，已经出现了count个1，当前是否受限制时的方案数
        # 使用字典进行记忆化
        memo = {}

        def dfs(i, count, is_limit):
            # 递归终止条件：已经处理完所有数位
            if i == length:
                return count

            # 记忆化：如果已经计算过该状态，直接返回结果
            if (i, count, is_limit) in memo and not is_limit:
                return memo[(i, count, is_limit)]

            # 确定当前位可以填入的数字范围
            # 如果受限制，最大只能填入s[i]对应的数字，否则可以填入0-9
            up = int(s[i]) if is_limit else 9
            ans = 0

            # 枚举当前位可以填入的数字
            for d in range(up + 1):
                # 递归处理下一位
                # 如果当前位填入1，则count+1
                # 下一位是否受限制：当前位受限制且填入了上限值
                ans += dfs(i + 1, count + (1 if d == 1 else 0), is_limit and d == up)

            # 记忆化存储结果
            if not is_limit:
                memo[(i, count, is_limit)] = ans
            return ans

        return dfs(0, 0, True)


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    n1 = 13
    print(f"n = {n1}, 数字1出现的次数: {solution.countDigitOne(n1)}")
    # 预期输出: 6 (数字1, 10, 11, 12, 13中1出现了6次)

    # 测试用例2
    n2 = 0
    print(f"n = {n2}, 数字1出现的次数: {solution.countDigitOne(n2)}")
    # 预期输出: 0

    # 测试用例3
    n3 = 100
    print(f"n = {n3}, 数字1出现的次数: {solution.countDigitOne(n3)}")
    # 预期输出: 21