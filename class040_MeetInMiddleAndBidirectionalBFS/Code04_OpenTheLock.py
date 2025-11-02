# 打开转盘锁
# 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。
# 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
# 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
# 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
# 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1 。
# 测试链接 : https://leetcode.cn/problems/open-the-lock/
# 
# 算法思路：
# 使用双向BFS算法，从起点"0000"和终点target同时开始搜索，一旦两个搜索相遇，就找到了最短路径
# 时间复杂度：O(10^4 * 8 + D)，其中D是deadends的长度，10^4是状态数，8是每个状态的邻居数
# 空间复杂度：O(10^4 + D)
# 
# 工程化考量：
# 1. 异常处理：检查初始状态是否在deadends中
# 2. 性能优化：使用双向BFS减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用set进行快速查找和去重，使用deque进行BFS操作

from typing import List
from collections import deque

def openLock(deadends: List[str], target: str) -> int:
    """
    计算打开转盘锁所需的最小旋转次数
    
    Args:
        deadends: 死亡数字列表
        target: 目标数字
    
    Returns:
        最小旋转次数，如果无法解锁返回-1
    """
    # 将deadends转换为set以提高查找效率
    deadSet = set(deadends)
    
    # 如果初始状态"0000"在deadends中，直接返回-1
    if "0000" in deadSet:
        return -1
    
    # 如果目标就是初始状态，返回0
    if target == "0000":
        return 0
    
    # 初始化双向BFS的队列和访问集合
    queue1 = deque(["0000"])  # 从起点开始的队列
    queue2 = deque([target])  # 从终点开始的队列
    visited1 = {"0000"}       # 从起点开始的访问集合
    visited2 = {target}       # 从终点开始的访问集合
    
    steps = 0
    
    # 双向BFS
    while queue1 and queue2:
        # 总是从较小的队列开始扩展，优化性能
        if len(queue1) > len(queue2):
            queue1, queue2 = queue2, queue1
            visited1, visited2 = visited2, visited1
        
        steps += 1
        size = len(queue1)
        
        # 扩展当前层的所有节点
        for _ in range(size):
            current = queue1.popleft()
            
            # 生成当前状态的所有邻居状态
            for next_state in getNeighbors(current):
                # 如果邻居状态在deadends中，跳过
                if next_state in deadSet:
                    continue
                
                # 如果邻居状态已经被访问过，跳过
                if next_state in visited1:
                    continue
                
                # 如果邻居状态在另一侧的访问集合中，说明两路相遇，返回步数
                if next_state in visited2:
                    return steps
                
                # 将邻居状态加入队列和访问集合
                queue1.append(next_state)
                visited1.add(next_state)
    
    return -1  # 无法解锁

def getNeighbors(s: str) -> List[str]:
    """
    生成当前状态的所有邻居状态
    
    Args:
        s: 当前状态
    
    Returns:
        邻居状态列表
    """
    neighbors = []
    chars = list(s)
    
    # 对每个位置尝试向上和向下旋转
    for i in range(4):
        original = chars[i]
        
        # 向上旋转
        chars[i] = str((int(original) + 1) % 10)
        neighbors.append("".join(chars))
        
        # 向下旋转
        chars[i] = str((int(original) + 9) % 10)
        neighbors.append("".join(chars))
        
        # 恢复原字符
        chars[i] = original
    
    return neighbors

# 测试方法
def main():
    # 测试用例1
    deadends1 = ["0201", "0101", "0102", "1212", "2002"]
    target1 = "0202"
    print("测试用例1:")
    print("deadends: [\"0201\",\"0101\",\"0102\",\"1212\",\"2002\"]")
    print("target: \"0202\"")
    print("期望输出: 6")
    print("实际输出:", openLock(deadends1, target1))
    print()
    
    # 测试用例2
    deadends2 = ["8888"]
    target2 = "0009"
    print("测试用例2:")
    print("deadends: [\"8888\"]")
    print("target: \"0009\"")
    print("期望输出: 1")
    print("实际输出:", openLock(deadends2, target2))
    print()
    
    # 测试用例3
    deadends3 = ["8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"]
    target3 = "8888"
    print("测试用例3:")
    print("deadends: [\"8887\",\"8889\",\"8878\",\"8898\",\"8788\",\"8988\",\"7888\",\"9888\"]")
    print("target: \"8888\"")
    print("期望输出: -1")
    print("实际输出:", openLock(deadends3, target3))

if __name__ == "__main__":
    main()