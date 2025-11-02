from collections import deque
from typing import List

# 打开转盘锁
# 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'。
# 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9'。每次旋转都只能旋转一个拨轮的一个数字。
# 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
# 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
# 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1。
# 测试链接 : https://leetcode.cn/problems/open-the-lock/
# 
# 算法思路：
# 使用BFS搜索从"0000"到target的最短路径。每个状态可以旋转8个方向（每个拨轮可以向上或向下旋转）。
# 使用哈希集合记录死亡数字和已访问状态，避免重复访问。
# 
# 时间复杂度：O(10^4 * 8) = O(80000)，因为有10000个可能的状态，每个状态有8个邻居
# 空间复杂度：O(10000)，用于存储队列和访问状态
# 
# 工程化考量：
# 1. 使用集合提高查找效率
# 2. 字符串操作优化
# 3. 边界情况处理
class Solution:
    def openLock(self, deadends: List[str], target: str) -> int:
        # 使用集合存储死亡数字，提高查找效率
        dead_set = set(deadends)
        start = "0000"
        
        # 边界情况：初始状态就是死亡数字
        if start in dead_set:
            return -1
        
        # 边界情况：初始状态就是目标状态
        if start == target:
            return 0
        
        # BFS队列和访问记录
        queue = deque()
        visited = set()
        
        queue.append(start)
        visited.add(start)
        steps = 0
        
        while queue:
            steps += 1
            size = len(queue)
            
            # 处理当前层的所有状态
            for _ in range(size):
                current = queue.popleft()
                
                # 生成所有可能的邻居状态
                for neighbor in self._get_neighbors(current):
                    # 跳过死亡数字和已访问状态
                    if neighbor in dead_set or neighbor in visited:
                        continue
                    
                    # 如果找到目标状态
                    if neighbor == target:
                        return steps
                    
                    # 加入队列并标记为已访问
                    visited.add(neighbor)
                    queue.append(neighbor)
        
        # 无法到达目标状态
        return -1
    
    def _get_neighbors(self, current: str) -> List[str]:
        """生成当前状态的所有邻居状态"""
        neighbors = []
        chars = list(current)
        
        # 对每个位置进行向上和向下旋转
        for i in range(4):
            original = chars[i]
            
            # 向上旋转（数字增加）
            chars[i] = str((int(original) + 1) % 10)
            neighbors.append(''.join(chars))
            
            # 向下旋转（数字减少）
            chars[i] = str((int(original) + 9) % 10)
            neighbors.append(''.join(chars))
            
            # 恢复原始字符
            chars[i] = original
        
        return neighbors

# 单元测试
def test_open_lock():
    solution = Solution()
    
    # 测试用例1：标准情况
    deadends1 = ["0201","0101","0102","1212","2002"]
    target1 = "0202"
    assert solution.openLock(deadends1, target1) == 6, "测试用例1失败"
    print("测试用例1通过")
    
    # 测试用例2：无法解锁
    deadends2 = ["8888"]
    target2 = "0009"
    assert solution.openLock(deadends2, target2) == 1, "测试用例2失败"
    print("测试用例2通过")
    
    # 测试用例3：初始状态就是死亡数字
    deadends3 = ["0000"]
    target3 = "8888"
    assert solution.openLock(deadends3, target3) == -1, "测试用例3失败"
    print("测试用例3通过")
    
    # 测试用例4：初始状态就是目标状态
    deadends4 = ["8888","9999"]
    target4 = "0000"
    assert solution.openLock(deadends4, target4) == 0, "测试用例4失败"
    print("测试用例4通过")
    
    # 测试用例5：复杂情况
    deadends5 = ["1000","0100","0010","0001","9000","0900","0090","0009"]
    target5 = "0002"
    assert solution.openLock(deadends5, target5) == 2, "测试用例5失败"
    print("测试用例5通过")
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    test_open_lock()