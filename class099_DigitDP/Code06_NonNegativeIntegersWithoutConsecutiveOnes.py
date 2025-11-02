# 不含连续1的非负整数
# 给定一个正整数n，返回在[0, n]范围内不含连续1的非负整数的个数
# 测试链接 : https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/


class Solution:
    def findIntegers(self, n: int) -> int:
        """
        数位DP方法
        时间复杂度: O(log n) 每个数位最多计算几次(受限/不受限, 前一位是0/1)
        空间复杂度: O(log n) 递归栈深度
        """
        # 将数字n转换为二进制字符串，方便按位处理
        s = bin(n)[2:]  # 去掉"0b"前缀
        length = len(s)
        
        # dp[i][prev][is_limit][is_num] 
        # 表示处理到第i位，前一位是prev，当前是否受限制，是否已填数字时的方案数
        # 使用字典进行记忆化
        memo = {}

        def dfs(i, prev, is_limit, is_num):
            # 递归终止条件：已经处理完所有数位
            if i == length:
                # 如果已经填了数字，返回1个有效数字，否则返回0
                return 1 if is_num else 0

            # 记忆化：如果已经计算过该状态，直接返回结果
            if (i, prev, is_limit, is_num) in memo and not is_limit and is_num:
                return memo[(i, prev, is_limit, is_num)]

            # 确定当前位可以填入的数字范围
            # 如果受限制，最大只能填入s[i]对应的数字，否则可以填入0-1
            up = int(s[i]) if is_limit else 1
            ans = 0

            # 如果前面没有填数字，可以跳过当前位（继续前导零）
            if not is_num:
                ans += dfs(i + 1, 0, False, False)

            # 枚举当前位可以填入的数字
            for d in range(0 if is_num else 1, up + 1):
                # 不能有连续的1
                if prev == 1 and d == 1:
                    continue
                # 递归处理下一位
                # 下一位是否受限制：当前位受限制且填入了上限值
                ans += dfs(i + 1, d, is_limit and d == up, True)

            # 记忆化存储结果
            if not is_limit and is_num:
                memo[(i, prev, is_limit, is_num)] = ans
            return ans

        return dfs(0, 0, True, False)


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    n1 = 5
    print(f"n = {n1}, 不含连续1的非负整数个数: {solution.findIntegers(n1)}")
    # 预期输出: 5 (0, 1, 2, 4, 5 满足条件，3的二进制是11，不满足)

    # 测试用例2
    n2 = 1
    print(f"n = {n2}, 不含连续1的非负整数个数: {solution.findIntegers(n2)}")
    # 预期输出: 2 (0, 1 满足条件)

    # 测试用例3
    n3 = 2
    print(f"n = {n3}, 不含连续1的非负整数个数: {solution.findIntegers(n3)}")
    # 预期输出: 3 (0, 1, 2 满足条件)