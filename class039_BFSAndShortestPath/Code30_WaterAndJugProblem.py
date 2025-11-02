# 水壶问题
# 有两个容量分别为 x 升 和 y 升 的水壶以及无限多的水。
# 请判断能否通过使用这两个水壶，从而可以得到恰好 z 升 的水？
# 如果可以，最后请用以上水壶中的一或两个来盛放取得的 z 升 水。
# 你允许：
# 装满任意一个水壶
# 清空任意一个水壶
# 从一个水壶向另外一个水壶倒水，直到装满或者倒空
# 测试链接 : https://leetcode.cn/problems/water-and-jug-problem/
# 
# 算法思路：
# 使用BFS搜索所有可能的状态。每个状态用(a, b)表示，其中a是第一个水壶的水量，b是第二个水壶的水量。
# 通过六种操作生成新的状态：装满A、装满B、倒空A、倒空B、A倒向B、B倒向A。
# 
# 时间复杂度：O(x * y)，状态空间为x*y
# 空间复杂度：O(x * y)，用于存储访问状态
# 
# 工程化考量：
# 1. 状态表示：使用元组记录访问状态
# 2. 操作模拟：精确模拟六种倒水操作
# 3. 数学优化：使用贝祖定理（裴蜀定理）进行数学判断
# 4. 边界情况：z为0，z大于x+y等特殊情况

from collections import deque

def canMeasureWater(x, y, z):
    """
    使用数学方法判断：贝祖定理
    
    Args:
        x: int - 第一个水壶的容量
        y: int - 第二个水壶的容量
        z: int - 目标水量
    
    Returns:
        bool - 是否可以测量出目标水量
    """
    # 边界情况处理
    if z == 0:
        return True
    if z > x + y:
        return False
    if x == 0 and y == 0:
        return z == 0
    
    # 使用数学方法判断：贝祖定理
    # z必须是x和y的最大公约数的倍数
    def gcd(a, b):
        if b == 0:
            return a
        return gcd(b, a % b)
    
    gcd_val = gcd(x, y)
    return z % gcd_val == 0

# BFS版本：用于理解和验证小规模数据
def canMeasureWaterBFS(x, y, z):
    """
    使用BFS搜索所有可能状态
    
    Args:
        x: int - 第一个水壶的容量
        y: int - 第二个水壶的容量
        z: int - 目标水量
    
    Returns:
        bool - 是否可以测量出目标水量
    """
    if z == 0:
        return True
    if z > x + y:
        return False
    if x == 0 and y == 0:
        return z == 0
    
    # 使用BFS搜索所有可能状态
    queue = deque([(0, 0)])
    visited = set()
    visited.add((0, 0))
    
    while queue:
        a, b = queue.popleft()
        
        # 检查是否达到目标
        if a == z or b == z or a + b == z:
            return True
        
        # 生成所有可能的操作
        next_states = []
        
        # 1. 装满A
        next_states.append((x, b))
        
        # 2. 装满B
        next_states.append((a, y))
        
        # 3. 倒空A
        next_states.append((0, b))
        
        # 4. 倒空B
        next_states.append((a, 0))
        
        # 5. A倒向B
        pour_ab = min(a, y - b)  # 可以倒出的水量
        next_states.append((a - pour_ab, b + pour_ab))
        
        # 6. B倒向A
        pour_ba = min(b, x - a)  # 可以倒出的水量
        next_states.append((a + pour_ba, b - pour_ba))
        
        for next_state in next_states:
            if next_state not in visited:
                visited.add(next_state)
                queue.append(next_state)
    
    return False

# 优化版本：使用数学方法 + BFS小规模验证
def canMeasureWaterOptimized(x, y, z):
    """
    优化版本：使用数学方法 + BFS小规模验证
    
    Args:
        x: int - 第一个水壶的容量
        y: int - 第二个水壶的容量
        z: int - 目标水量
    
    Returns:
        bool - 是否可以测量出目标水量
    """
    # 数学方法快速判断
    if z == 0:
        return True
    if z > x + y:
        return False
    if x == 0:
        return z == y or z == 0
    if y == 0:
        return z == x or z == 0
    
    def gcd(a, b):
        if b == 0:
            return a
        return gcd(b, a % b)
    
    gcd_val = gcd(x, y)
    if z % gcd_val != 0:
        return False
    
    # 对于小规模数据，使用BFS验证
    if x * y <= 1000000:
        return canMeasureWaterBFS(x, y, z)
    
    return True

# 测试代码
if __name__ == "__main__":
    # 测试用例1：标准情况
    print("测试用例1 - 是否可以测量:", canMeasureWater(3, 5, 4))  # 期望输出: True
    
    # 测试用例2：无法测量
    print("测试用例2 - 是否可以测量:", canMeasureWater(2, 6, 5))  # 期望输出: False
    
    # 测试用例3：边界情况
    print("测试用例3 - 是否可以测量:", canMeasureWater(0, 0, 0))  # 期望输出: True
    
    # 测试用例4：贝祖定理验证
    print("测试用例4 - 是否可以测量:", canMeasureWater(4, 6, 8))  # 期望输出: True