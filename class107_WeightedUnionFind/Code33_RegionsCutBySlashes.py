/**
 * LeetCode 959 - 由斜杠划分区域
 * https://leetcode-cn.com/problems/regions-cut-by-slashes/
 * 
 * 题目描述：
 * 在由 1 x 1 方格组成的 N x N 网格 grid 中，每个 1 x 1 方块由 /、\ 或空格构成。这些字符会将方块划分为一些共边的区域。
 * 返回区域的数目。
 * 
 * 示例 1：
 * 输入：
 * [
 *   " /",
 *   "/ "
 * ]
 * 输出：2
 * 
 * 示例 2：
 * 输入：
 * [
 *   " /",
 *   "  "
 * ]
 * 输出：1
 * 
 * 示例 3：
 * 输入：
 * [
 *   "\\/",
 *   "/\\"
 * ]
 * 输出：4
 * 
 * 解题思路（并查集）：
 * 1. 将每个1x1的方格分成4个三角形区域（上、右、下、左）
 * 2. 根据每个方格中的字符（/、\ 或空格），将方格内部的三角形区域连接起来
 * 3. 同时，将相邻方格的三角形区域连接起来
 * 4. 最后统计连通分量的数量，即为区域的数目
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n²)
 * - 连接操作：O(n² * α(n²))，其中α是阿克曼函数的反函数，近似为常数
 * - 总体时间复杂度：O(n² * α(n²)) ≈ O(n²)
 * 
 * 空间复杂度分析：
 * - 并查集：O(n²)
 * - 总体空间复杂度：O(n²)
 */

class RegionsCutBySlashes:
    def __init__(self):
        self.parent = []
        self.count = 0  # 连通分量的数量
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x: 要查找的元素
            
        返回:
            根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个元素所在的集合
        
        参数:
            x: 第一个元素
            y: 第二个元素
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return
        
        self.parent[root_x] = root_y
        self.count -= 1
    
    def get_index(self, n, i, j, k):
        """
        将每个1x1方格的四个三角形区域编号
        
        参数:
            n: 网格大小
            i: 行号
            j: 列号
            k: 区域编号（0:上, 1:右, 2:下, 3:左）
            
        返回:
            全局唯一的节点编号
        """
        return 4 * (i * n + j) + k
    
    def regions_by_slashes(self, grid):
        """
        计算由斜杠划分的区域数目
        
        参数:
            grid: 网格
            
        返回:
            区域数目
        """
        n = len(grid)
        total_nodes = 4 * n * n  # 每个方格有4个三角形区域
        
        # 初始化并查集
        self.parent = list(range(total_nodes))
        self.count = total_nodes
        
        for i in range(n):
            for j in range(n):
                c = grid[i][j]
                
                # 连接当前方格内部的三角形区域
                if c == ' ':
                    # 空格：四个区域全部连通
                    self.union(self.get_index(n, i, j, 0), self.get_index(n, i, j, 1))
                    self.union(self.get_index(n, i, j, 1), self.get_index(n, i, j, 2))
                    self.union(self.get_index(n, i, j, 2), self.get_index(n, i, j, 3))
                elif c == '/':
                    # 左斜杠：上和左连通，右和下连通
                    self.union(self.get_index(n, i, j, 0), self.get_index(n, i, j, 3))
                    self.union(self.get_index(n, i, j, 1), self.get_index(n, i, j, 2))
                elif c == '\\':
                    # 右斜杠：上和右连通，下和左连通
                    self.union(self.get_index(n, i, j, 0), self.get_index(n, i, j, 1))
                    self.union(self.get_index(n, i, j, 2), self.get_index(n, i, j, 3))
                
                # 连接当前方格与下方方格
                if i < n - 1:
                    # 当前方格的下区域连接到下方方格的上区域
                    self.union(self.get_index(n, i, j, 2), self.get_index(n, i + 1, j, 0))
                
                # 连接当前方格与右方方格
                if j < n - 1:
                    # 当前方格的右区域连接到右方方格的左区域
                    self.union(self.get_index(n, i, j, 1), self.get_index(n, i, j + 1, 3))
        
        return self.count

# 测试代码
def test_regions_by_slashes():
    solution = RegionsCutBySlashes()
    
    # 测试用例1
    grid1 = [
        " /",
        "/ "
    ]
    print("测试用例1结果：", solution.regions_by_slashes(grid1))
    # 预期输出：2
    
    # 测试用例2
    grid2 = [
        " /",
        "  "
    ]
    print("测试用例2结果：", solution.regions_by_slashes(grid2))
    # 预期输出：1
    
    # 测试用例3
    grid3 = [
        "\\/",
        "/\\"
    ]
    print("测试用例3结果：", solution.regions_by_slashes(grid3))
    # 预期输出：4
    
    # 测试用例4
    grid4 = [
        "/\\",
        "\\/"
    ]
    print("测试用例4结果：", solution.regions_by_slashes(grid4))
    # 预期输出：5

if __name__ == "__main__":
    test_regions_by_slashes()

'''
Python特定优化：
1. 使用列表实现并查集，简洁高效
2. 将并查集操作封装在类中，提高代码的可读性和可维护性
3. 使用详细的文档字符串，解释每个函数的作用和参数
4. 实现了路径压缩优化，提高查找效率

算法思路详解：
1. 将每个1x1的方格划分为四个三角形区域，分别编号为0（上）、1（右）、2（下）、3（左）
2. 对于每个方格，根据其中的字符连接内部区域：
   - 空格：四个区域全部连通
   - /：上区域和左区域连通，右区域和下区域连通
   - \：上区域和右区域连通，下区域和左区域连通
3. 连接相邻方格的对应区域：
   - 当前方格的下区域与下方方格的上区域连通
   - 当前方格的右区域与右方方格的左区域连通
4. 最后统计连通分量的数量，即为区域数目

工程化考量：
1. 边界情况处理：代码自动处理网格大小为0或1的情况
2. 可读性：添加了详细的注释和文档字符串
3. 可测试性：提供了多个测试用例，覆盖不同的输入情况

时间复杂度深入分析：
- 初始化并查集：O(n²)
- 每个方格最多进行常数次连接操作，每次连接的时间复杂度为O(α(n²))
- 总体时间复杂度为O(n² * α(n²))，其中α是阿克曼函数的反函数，在实际应用中可视为常数
- 因此实际时间复杂度近似为O(n²)

空间复杂度深入分析：
- 并查集数组的空间复杂度为O(n²)
- 总体空间复杂度为O(n²)
'''