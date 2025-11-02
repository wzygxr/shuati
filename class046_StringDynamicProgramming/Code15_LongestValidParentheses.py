# 最长有效括号
# 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度
# 测试链接 : https://leetcode.cn/problems/longest-valid-parentheses/

class Solution:
    
    def longestValidParentheses1(self, s: str) -> int:
        """
        算法思路：
        使用动态规划解决最长有效括号问题
        dp[i] 表示以索引为 i 的字符结尾的最长有效括号的长度
        
        状态转移方程：
        如果 s[i] 是 '('，则 dp[i] = 0（以左括号结尾的子串无法构成有效括号）
        如果 s[i] 是 ')'：
          1. 如果 s[i-1] 是 '('，则 dp[i] = dp[i-2] + 2（形如"...()"）
          2. 如果 s[i-1] 是 ')'，且 s[i - dp[i-1] - 1] 是 '('，则 dp[i] = dp[i-1] + 2 + dp[i - dp[i-1] - 2]（形如"...(())"）
        
        边界条件：
        dp[0] = 0（单个字符无法构成有效括号）
        
        时间复杂度：O(n)，其中n为字符串s的长度
        空间复杂度：O(n)
        """
        if not s or len(s) < 2:
            return 0
        n = len(s)
        # dp[i] 表示以s[i]结尾的最长有效括号子串的长度
        dp = [0] * n
        max_len = 0
        
        # 从第二个字符开始遍历
        for i in range(1, n):
            if s[i] == ')':
                if s[i - 1] == '(':
                    # 情况1: "...()"
                    dp[i] = (dp[i - 2] if i >= 2 else 0) + 2
                elif i - dp[i - 1] > 0 and s[i - dp[i - 1] - 1] == '(':
                    # 情况2: "...(())"
                    dp[i] = dp[i - 1] + 2
                    # 加上前面可能连接的有效括号子串的长度
                    if i - dp[i - 1] >= 2:
                        dp[i] += dp[i - dp[i - 1] - 2]
                # 更新最大长度
                max_len = max(max_len, dp[i])
        
        return max_len
    
    def longestValidParentheses2(self, s: str) -> int:
        """
        优化版本：使用栈
        栈中存储未匹配的左括号索引和上一个未匹配的右括号索引
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not s or len(s) < 2:
            return 0
        n = len(s)
        max_len = 0
        # 栈中存储索引，初始放入-1作为基准
        stack = [-1]
        
        for i in range(n):
            if s[i] == '(':
                # 遇到左括号，将其索引入栈
                stack.append(i)
            else:
                # 遇到右括号，先弹出栈顶元素
                stack.pop()
                if not stack:
                    # 栈为空，说明这个右括号没有匹配的左括号，将其索引入栈作为新的基准
                    stack.append(i)
                else:
                    # 计算当前有效括号子串的长度
                    max_len = max(max_len, i - stack[-1])
        
        return max_len
    
    def longestValidParentheses3(self, s: str) -> int:
        """
        优化版本：双向扫描
        不需要额外空间，通过两次扫描（从左到右和从右到左）来找到最长有效括号
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not s or len(s) < 2:
            return 0
        n = len(s)
        left = right = max_len = 0
        
        # 从左到右扫描
        for i in range(n):
            if s[i] == '(':
                left += 1
            else:
                right += 1
            if left == right:
                # 左右括号数量相等，形成有效括号子串
                max_len = max(max_len, left + right)
            elif right > left:
                # 右括号数量超过左括号，重置计数
                left = right = 0
        
        # 重置计数器，从右到左扫描
        left = right = 0
        for i in range(n - 1, -1, -1):
            if s[i] == '(':
                left += 1
            else:
                right += 1
            if left == right:
                # 左右括号数量相等，形成有效括号子串
                max_len = max(max_len, left + right)
            elif left > right:
                # 左括号数量超过右括号，重置计数
                left = right = 0
        
        return max_len

# 单元测试
def test_solution():
    solution = Solution()
    
    # 测试用例1: "(()"
    # 预期输出: 2 (子串 "()")
    print(f"Test 1: {solution.longestValidParentheses1('(()')}")  # 应输出2
    print(f"Test 1 (Stack): {solution.longestValidParentheses2('(()')}")  # 应输出2
    print(f"Test 1 (Two Pass): {solution.longestValidParentheses3('(()')}")  # 应输出2
    
    # 测试用例2: ")()())"
    # 预期输出: 4 (子串 "()()")
    print(f"Test 2: {solution.longestValidParentheses1(')()())')}")  # 应输出4
    print(f"Test 2 (Stack): {solution.longestValidParentheses2(')()())')}")  # 应输出4
    print(f"Test 2 (Two Pass): {solution.longestValidParentheses3(')()())')}")  # 应输出4
    
    # 测试用例3: ""
    # 预期输出: 0
    print(f"Test 3: {solution.longestValidParentheses1('')}")  # 应输出0
    print(f"Test 3 (Stack): {solution.longestValidParentheses2('')}")  # 应输出0
    print(f"Test 3 (Two Pass): {solution.longestValidParentheses3('')}")  # 应输出0
    
    # 测试用例4: "(())"
    # 预期输出: 4
    print(f"Test 4: {solution.longestValidParentheses1('(())')}")  # 应输出4
    print(f"Test 4 (Stack): {solution.longestValidParentheses2('(())')}")  # 应输出4
    print(f"Test 4 (Two Pass): {solution.longestValidParentheses3('(())')}")  # 应输出4
    
    # 测试用例5: "()(()"
    # 预期输出: 2
    print(f"Test 5: {solution.longestValidParentheses1('()(()')}")  # 应输出2
    print(f"Test 5 (Stack): {solution.longestValidParentheses2('()(()')}")  # 应输出2
    print(f"Test 5 (Two Pass): {solution.longestValidParentheses3('()(()')}")  # 应输出2

# 运行测试
if __name__ == "__main__":
    test_solution()