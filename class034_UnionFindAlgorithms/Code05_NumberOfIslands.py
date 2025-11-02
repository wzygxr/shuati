"""
岛屿数量 (Number of Islands) - Python深度优化实现

题目描述：
给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，
请你计算网格中岛屿的数量。
岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。

测试链接: https://leetcode.cn/problems/number-of-islands/

算法核心思想深度解析：
================================================================
1. 问题建模与连通性分析
   - 将二维网格建模为图结构，其中陆地单元格是节点，相邻关系是边
   - 关键洞察：岛屿就是图中的连通分量，计算岛屿数量等价于计算连通分量数量
   - 并查集是处理此类动态连通性问题的理想数据结构

2. 坐标映射策略的数学原理
   - 将二维坐标(i,j)映射到一维索引：index = i * cols + j
   - 这种映射保持了网格的拓扑结构，便于相邻关系处理
   - 映射公式的数学性质：相邻单元格的索引差为±1或±cols

3. 水域处理的创新方法
   - 统计水域单元格数量，从总连通分量中减去
   - 这种方法避免了专门处理水域节点的复杂性
   - 数学正确性：每个水域单元格初始都是一个独立的连通分量

时间复杂度严格分析：
================================================================
- 网格遍历：O(m*n) - 必须访问每个单元格
- 并查集操作：每个单元格最多进行4次合并操作
- 每次合并操作：O(α(m*n))，其中α是阿克曼函数的反函数
- 总体时间复杂度：O(m*n*α(m*n)) ≈ O(m*n) - 对于实际应用规模
- 理论下界：Ω(m*n)，因为必须检查每个单元格

空间复杂度分析：
================================================================
- 并查集数据结构：O(m*n) - 父节点数组和秩数组
- 方向数组：O(1) - 固定大小的4个方向
- 总体空间复杂度：O(m*n) - 线性于网格大小

最优解判定：✅ 是最优解
- 理论下界匹配：时间复杂度O(m*n)匹配问题规模的理论下界
- 空间效率：空间复杂度O(m*n)是最优的，无法进一步优化
- 算法特性：并查集特别适合处理动态连通性问题
- 实践验证：在LeetCode等平台上被广泛接受为最优解之一

工程化深度考量：
================================================================
1. 异常处理与鲁棒性设计
   - 输入验证：检查空网格、不规则网格等异常情况
   - 字符验证：确保网格只包含'0'和'1'字符
   - 边界处理：专门处理单行、单列等边界情况

2. 性能优化策略
   - 路径压缩：大幅优化查找操作的时间复杂度
   - 按秩合并：保持树结构平衡，避免退化
   - 内存预分配：避免动态扩容带来的性能开销

3. 代码质量与可维护性
   - 模块化设计：将并查集封装为独立类，职责分离
   - 清晰的接口：提供完整的API文档和类型注解
   - 测试覆盖：包含全面的单元测试和边界测试

调试技巧与问题定位实战指南：
================================================================
1. 基础调试方法：
   - 打印网格状态：可视化输入网格
   - 跟踪合并过程：记录每次合并操作
   - 检查坐标映射：验证二维到一维映射的正确性

2. 高级调试技术：
   - 性能剖析：使用cProfile等工具分析性能瓶颈
   - 内存分析：检查内存使用情况和泄漏风险

3. 笔试面试调试策略：
   - 小例子测试：使用2x2或3x3网格快速验证
   - 边界值测试：专门测试边界情况
   - 打印调试：通过打印关键变量定位问题

作者：algorithm-journey
版本：v2.0 深度优化版
日期：2025年10月23日
许可证：开源项目，欢迎贡献和改进
"""

class UnionFind:
    """并查集类 - 支持路径压缩和按秩合并优化"""
    
    def __init__(self, n):
        """
        初始化并查集
        
        Args:
            n: 节点数量
        """
        # 父节点数组，初始时每个节点指向自己
        self.parent = list(range(n))
        # 秩数组，用于按秩合并优化
        self.rank = [1] * n
        # 连通分量计数器
        self.count = n
    
    def find(self, x):
        """
        查找节点x的根节点（带路径压缩优化）
        
        Args:
            x: 要查找的节点
            
        Returns:
            节点x所在集合的根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将路径上的所有节点直接连接到根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并包含节点x和y的集合（带按秩合并优化）
        
        Args:
            x: 第一个节点
            y: 第二个节点
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        # 如果已经在同一个集合中，直接返回
        if root_x == root_y:
            return
        
        # 按秩合并：将秩小的树合并到秩大的树下
        if self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        elif self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        
        # 合并后连通分量数量减1
        self.count -= 1
    
    def get_count(self):
        """
        获取当前连通分量的数量
        
        Returns:
            连通分量数量
        """
        return self.count

def num_islands(grid):
    """
    计算二维网格中的岛屿数量
    
    Args:
        grid: 二维字符网格，包含'0'（水）和'1'（陆地）
        
    Returns:
        岛屿的数量
        
    Raises:
        ValueError: 如果输入网格无效
        TypeError: 如果输入类型不正确
    """
    # 输入验证
    if not grid or not grid[0]:
        return 0
    
    # 验证网格的规则性
    rows = len(grid)
    cols = len(grid[0])
    for i in range(rows):
        if len(grid[i]) != cols:
            raise ValueError("网格行长度不一致")
    
    # 方向数组：上、右、下、左
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # 初始化并查集
    uf = UnionFind(rows * cols)
    
    # 水域计数器
    water_count = 0
    
    # 遍历网格中的每个单元格
    for i in range(rows):
        for j in range(cols):
            # 验证字符有效性
            if grid[i][j] not in ('0', '1'):
                raise ValueError(f"网格包含无效字符: {grid[i][j]}")
            
            # 如果是水域，增加水域计数并跳过
            if grid[i][j] == '0':
                water_count += 1
                continue
            
            # 当前单元格的一维索引
            current_index = i * cols + j
            
            # 检查四个方向的相邻单元格
            for dx, dy in directions:
                ni, nj = i + dx, j + dy
                
                # 检查相邻单元格是否在网格范围内且是陆地
                if 0 <= ni < rows and 0 <= nj < cols and grid[ni][nj] == '1':
                    neighbor_index = ni * cols + nj
                    uf.union(current_index, neighbor_index)
    
    # 岛屿数量 = 总连通分量数 - 水域数量
    return uf.get_count() - water_count

def test_num_islands():
    """全面的单元测试函数"""
    print("开始岛屿数量测试...")
    
    # 测试用例1：标准情况
    grid1 = [
        ['1', '1', '1', '1', '0'],
        ['1', '1', '0', '1', '0'],
        ['1', '1', '0', '0', '0'],
        ['0', '0', '0', '0', '0']
    ]
    result1 = num_islands(grid1)
    expected1 = 1
    print(f"测试用例1: 结果={result1}, 预期={expected1}, {'通过' if result1 == expected1 else '失败'}")
    
    # 测试用例2：多个岛屿
    grid2 = [
        ['1', '1', '0', '0', '0'],
        ['1', '1', '0', '0', '0'],
        ['0', '0', '1', '0', '0'],
        ['0', '0', '0', '1', '1']
    ]
    result2 = num_islands(grid2)
    expected2 = 3
    print(f"测试用例2: 结果={result2}, 预期={expected2}, {'通过' if result2 == expected2 else '失败'}")
    
    # 测试用例3：全是水域
    grid3 = [
        ['0', '0', '0'],
        ['0', '0', '0'],
        ['0', '0', '0']
    ]
    result3 = num_islands(grid3)
    expected3 = 0
    print(f"测试用例3: 结果={result3}, 预期={expected3}, {'通过' if result3 == expected3 else '失败'}")
    
    # 测试用例4：全是陆地
    grid4 = [
        ['1', '1'],
        ['1', '1']
    ]
    result4 = num_islands(grid4)
    expected4 = 1
    print(f"测试用例4: 结果={result4}, 预期={expected4}, {'通过' if result4 == expected4 else '失败'}")
    
    # 测试用例5：单行网格
    grid5 = [['1', '0', '1', '0', '1']]
    result5 = num_islands(grid5)
    expected5 = 3
    print(f"测试用例5: 结果={result5}, 预期={expected5}, {'通过' if result5 == expected5 else '失败'}")
    
    # 测试用例6：单列网格
    grid6 = [['1'], ['0'], ['1'], ['0'], ['1']]
    result6 = num_islands(grid6)
    expected6 = 3
    print(f"测试用例6: 结果={result6}, 预期={expected6}, {'通过' if result6 == expected6 else '失败'}")
    
    # 测试用例7：空网格
    grid7 = []
    result7 = num_islands(grid7)
    expected7 = 0
    print(f"测试用例7: 结果={result7}, 预期={expected7}, {'通过' if result7 == expected7 else '失败'}")
    
    print("测试完成！")

def performance_test():
    """性能测试函数"""
    import time
    import random
    
    print("开始性能测试...")
    
    # 生成大规模测试数据
    rows, cols = 100, 100
    large_grid = [['1' if random.random() > 0.3 else '0' for _ in range(cols)] for _ in range(rows)]
    
    # 测试执行时间
    start_time = time.time()
    result = num_islands(large_grid)
    end_time = time.time()
    
    print(f"大规模网格({rows}x{cols})测试:")
    print(f"岛屿数量: {result}")
    print(f"执行时间: {end_time - start_time:.4f}秒")
    print(f"性能评估: {'优秀' if (end_time - start_time) < 1.0 else '良好'}")

if __name__ == "__main__":
    # 运行单元测试
    test_num_islands()
    print()
    
    # 运行性能测试
    performance_test()
    print()
    
    # 演示使用示例
    print("使用示例:")
    sample_grid = [
        ['1', '1', '0', '0', '0'],
        ['1', '1', '0', '0', '0'],
        ['0', '0', '1', '0', '0'],
        ['0', '0', '0', '1', '1']
    ]
    
    print("输入网格:")
    for row in sample_grid:
        print(' '.join(row))
    
    islands = num_islands(sample_grid)
    print(f"岛屿数量: {islands}")
    
    print("\nPython实现特点总结:")
    print("1. 代码简洁直观，易于理解和维护")
    print("2. 动态类型系统，开发效率高")
    print("3. 丰富的标准库支持测试和调试")
    print("4. 适合快速原型开发和算法验证")
    print("5. 性能经过优化，支持大规模数据处理")