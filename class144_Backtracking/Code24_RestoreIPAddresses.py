from typing import List

class Solution:
    """
    LeetCode 93. 复原 IP 地址
    
    题目描述：
    有效 IP 地址 正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。
    例如："0.1.2.201" 和 "192.168.1.1" 是 有效 IP 地址，
    但是 "0.011.255.245"、"192.168.1.312" 和 "192.168@1.1" 是 无效 IP 地址。
    给定一个只包含数字的字符串 s ，用以表示一个 IP 地址，返回所有可能的有效 IP 地址。
    
    示例：
    输入：s = "25525511135"
    输出：["255.255.11.135","255.255.111.35"]
    
    输入：s = "0000"
    输出：["0.0.0.0"]
    
    输入：s = "101023"
    输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
    
    提示：
    1 <= s.length <= 20
    s 仅由数字组成
    
    链接：https://leetcode.cn/problems/restore-ip-addresses/
    
    算法思路：
    1. 使用回溯算法分割字符串为4个部分
    2. 每个部分必须满足：0-255之间，不能有前导0（除非是0本身）
    3. 当分割完成4个部分且字符串用完时，加入结果集
    4. 使用剪枝优化：剩余字符串长度不足以填满剩余部分时提前终止
    
    时间复杂度：O(3^4) = O(81)，每个部分最多3位数字，共4个部分
    空间复杂度：O(n)，递归栈深度
    """
    
    def restoreIpAddresses(self, s: str) -> List[str]:
        result = []
        self.backtrack(s, 0, [], result)
        return result
    
    def backtrack(self, s: str, start: int, path: List[str], result: List[str]) -> None:
        # 终止条件：已经分割成4个部分
        if len(path) == 4:
            # 如果字符串刚好用完，加入结果集
            if start == len(s):
                result.append('.'.join(path))
            return
        
        # 剪枝：剩余字符串长度不足以填满剩余部分
        # 剩余部分数：4 - len(path)
        # 每个部分最少1位，最多3位
        min_remaining = 4 - len(path)
        max_remaining = 3 * (4 - len(path))
        remaining_length = len(s) - start
        
        if remaining_length < min_remaining or remaining_length > max_remaining:
            return
        
        # 尝试取1位、2位、3位数字
        for length in range(1, 4):
            # 检查是否超出字符串长度
            if start + length > len(s):
                break
            
            # 获取当前部分
            segment = s[start:start + length]
            
            # 检查是否有效
            if self.is_valid_segment(segment):
                path.append(segment)
                self.backtrack(s, start + length, path, result)
                path.pop()
    
    def is_valid_segment(self, segment: str) -> bool:
        # 检查长度
        if len(segment) == 0 or len(segment) > 3:
            return False
        
        # 检查前导0
        if len(segment) > 1 and segment[0] == '0':
            return False
        
        # 检查数值范围
        num = int(segment)
        return 0 <= num <= 255

def test_restore_ip_addresses():
    solution = Solution()
    
    # 测试用例1
    s1 = "25525511135"
    result1 = solution.restoreIpAddresses(s1)
    print(f'输入: s = "{s1}"')
    print("输出:", result1)
    
    # 测试用例2
    s2 = "0000"
    result2 = solution.restoreIpAddresses(s2)
    print(f'\n输入: s = "{s2}"')
    print("输出:", result2)
    
    # 测试用例3
    s3 = "101023"
    result3 = solution.restoreIpAddresses(s3)
    print(f'\n输入: s = "{s3}"')
    print("输出:", result3)

if __name__ == "__main__":
    test_restore_ip_addresses()