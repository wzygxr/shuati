"""
表格填数问题 - Python实现
给定一个长度为n的数组arr，arr[i]表示i位置上方的正方形格子数量
在这片区域中，你要放入k个相同数字，不能有任意两个数字在同一行或者同一列
注意在这片区域中，如果某一行中间断开了，使得两个数字无法在这一行连通，则不算违规
返回填入数字的方法数，答案对 1000000007 取模
1 <= n、k <= 500    0 <= arr[i] <= 10^6
测试链接 : https://www.luogu.com.cn/problem/P6453

算法思路：
1. 使用笛卡尔树对直方图进行分解
2. 结合组合数学和动态规划计算填数方案
3. 时间复杂度：O(n^2 * k) 或 O(n * k^2)
4. 空间复杂度：O(n * k)

工程化考量：
- 使用记忆化递归避免重复计算
- 预处理阶乘和逆元提高计算效率
- 注意Python的递归深度限制，对于大数据需要优化
"""

MOD = 1000000007
MAXN = 501
MAXH = 1000000

# 预处理阶乘和逆元表
fac = [1] * (MAXH + 1)
inv = [1] * (MAXH + 1)

# 快速幂计算
def power(x, p):
    """快速幂计算 x^p % MOD"""
    result = 1
    while p > 0:
        if p & 1:
            result = (result * x) % MOD
        x = (x * x) % MOD
        p >>= 1
    return result

# 组合数计算
def comb(n, k):
    """计算组合数 C(n, k) % MOD"""
    if n < k:
        return 0
    return fac[n] * inv[k] % MOD * inv[n - k] % MOD

# 预处理阶乘和逆元
def precompute():
    """预处理阶乘和逆元表"""
    # 计算阶乘
    for i in range(2, MAXH + 1):
        fac[i] = fac[i - 1] * i % MOD
    
    # 计算逆元
    inv[MAXH] = power(fac[MAXH], MOD - 2)
    for i in range(MAXH - 1, 0, -1):
        inv[i] = inv[i + 1] * (i + 1) % MOD

# 笛卡尔树节点类
class TreeNode:
    def __init__(self, val=0, idx=0):
        self.val = val
        self.idx = idx
        self.left = None
        self.right = None
        self.size = 0

# 构建笛卡尔树
def build_cartesian_tree(arr):
    """
    构建笛卡尔树
    Args:
        arr: 输入数组，arr[0]不使用，从arr[1]开始
    Returns:
        笛卡尔树的根节点
    """
    n = len(arr) - 1
    stack = []
    nodes = [None] * (n + 1)
    
    # 创建节点
    for i in range(1, n + 1):
        nodes[i] = TreeNode(arr[i], i)
    
    # 构建笛卡尔树
    for i in range(1, n + 1):
        last = None
        while stack and arr[stack[-1]] > arr[i]:
            last = stack.pop()
        
        if stack:
            nodes[stack[-1]].right = nodes[i]
        
        if last is not None:
            nodes[i].left = nodes[last]
        
        stack.append(i)
    
    return nodes[stack[0]] if stack else None

# DFS遍历笛卡尔树进行动态规划
def dfs(node, parent_val, dp, k):
    """
    DFS遍历笛卡尔树进行动态规划
    Args:
        node: 当前节点
        parent_val: 父节点的值
        dp: 动态规划表，dp[node][i]表示在node子树中放i个数字的方案数
        k: 要放置的数字数量
    Returns:
        子树的大小
    """
    if node is None:
        return 0
    
    # 递归处理左右子树
    left_size = dfs(node.left, node.val, dp, k)
    right_size = dfs(node.right, node.val, dp, k)
    
    # 计算当前子树大小
    node.size = left_size + right_size + 1
    
    # 临时数组存储合并结果
    tmp = [0] * (k + 1)
    
    # 合并左右子树的DP结果
    for l in range(min(left_size, k) + 1):
        for r in range(min(right_size, k - l) + 1):
            if l + r <= k:
                left_dp = dp.get(node.left, [0] * (k + 1))[l] if node.left else (1 if l == 0 else 0)
                right_dp = dp.get(node.right, [0] * (k + 1))[r] if node.right else (1 if r == 0 else 0)
                tmp[l + r] = (tmp[l + r] + left_dp * right_dp) % MOD
    
    # 计算当前节点的DP值
    node_dp = [0] * (k + 1)
    for i in range(min(node.size, k) + 1):
        for p in range(min(i, node.size) + 1):
            if i - p < 0:
                continue
            
            # 计算组合数方案
            ways = comb(node.size - p, i - p) * comb(node.val - parent_val, i - p) % MOD
            ways = ways * fac[i - p] % MOD
            
            if p < len(tmp):
                node_dp[i] = (node_dp[i] + ways * tmp[p] % MOD) % MOD
    
    dp[node] = node_dp
    return node.size

def main():
    """主函数"""
    import sys
    
    # 预处理阶乘和逆元
    precompute()
    
    # 读取输入
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    k = int(data[1])
    
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[i + 1])
    
    # 构建笛卡尔树
    root = build_cartesian_tree(arr)
    
    # 动态规划表
    dp = {}
    
    # 空节点的DP值
    dp[None] = [1] + [0] * k
    
    # DFS计算DP
    if root:
        dfs(root, 0, dp, k)
        result = dp[root][k] if k < len(dp[root]) else 0
    else:
        result = 0
    
    print(result)

if __name__ == "__main__":
    main()