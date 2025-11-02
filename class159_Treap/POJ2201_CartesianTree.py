# POJ 2201 Cartesian Tree
# 给定 n 对 (key, value)，构建笛卡尔树，满足 key 满足二叉搜索树性质，value 满足堆性质
# 测试链接 : http://poj.org/problem?id=2201

MAXN = 50001

# 节点信息
key = [0] * MAXN      # key值，满足二叉搜索树性质
value = [0] * MAXN    # value值，满足堆性质
parent = [0] * MAXN   # 父节点
left_child = [0] * MAXN     # 左子节点
right_child = [0] * MAXN    # 右子节点

# 单调栈
stack = [0] * MAXN

# 构建笛卡尔树
def buildCartesianTree(n):
    """
    构建笛卡尔树
    核心思想：
    1. 按key值对节点进行排序
    2. 使用单调栈按value值构建满足堆性质的树结构
    :param n: 节点数量
    """
    # 初始化所有节点的父节点和子节点为0（空节点）
    for i in range(1, n+1):
        parent[i] = 0
        left_child[i] = 0
        right_child[i] = 0
    
    # 创建节点列表并按key值排序，保证二叉搜索树性质
    nodes = []
    for i in range(1, n+1):
        nodes.append((key[i], value[i], i))
    
    nodes.sort()
    
    # 使用单调栈构建笛卡尔树（按value值构建堆性质）
    top = 0  # 栈顶指针
    for i in range(n):
        key_val, value_val, idx = nodes[i]
        pos = top
        
        # 维护单调栈，弹出value值大于当前节点的节点
        # 保证栈中节点的value按从小到大排列（小根堆性质）
        while pos > 0 and value[stack[pos]] > value_val:
            pos -= 1
        
        # 建立父子关系
        if pos > 0:
            # 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
            parent[idx] = stack[pos]
            right_child[stack[pos]] = idx
        
        if pos < top:
            # 当前节点的左子节点是最后被弹出的节点
            parent[stack[pos + 1]] = idx
            left_child[idx] = stack[pos + 1]
        
        # 将当前节点压入栈中
        stack[pos + 1] = idx
        # 更新栈顶指针
        top = pos + 1

def main():
    """
    主函数
    """
    n = int(input())
    
    # 读取节点的key和value值
    for i in range(1, n+1):
        k, v = map(int, input().split())
        key[i] = k
        value[i] = v
    
    # 构建笛卡尔树
    buildCartesianTree(n)
    
    # 输出结果
    print("YES")
    for i in range(1, n+1):
        # 输出每个节点的父节点、左子节点、右子节点
        print(parent[i], left_child[i], right_child[i])

if __name__ == "__main__":
    main()