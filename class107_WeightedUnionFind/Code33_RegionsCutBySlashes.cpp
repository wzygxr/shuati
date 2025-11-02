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

#include <iostream>
#include <vector>
#include <string>

using namespace std;

class RegionsCutBySlashes {
private:
    vector<int> parent;
    int count; // 连通分量的数量

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     */
    void unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        parent[rootX] = rootY;
        count--;
    }

    /**
     * 将每个1x1方格的四个三角形区域编号
     * @param n 网格大小
     * @param i 行号
     * @param j 列号
     * @param k 区域编号（0:上, 1:右, 2:下, 3:左）
     * @return 全局唯一的节点编号
     */
    int getIndex(int n, int i, int j, int k) const {
        return 4 * (i * n + j) + k;
    }

public:
    /**
     * 计算由斜杠划分的区域数目
     * @param grid 网格
     * @return 区域数目
     */
    int regionsBySlashes(vector<string>& grid) {
        int n = grid.size();
        int totalNodes = 4 * n * n; // 每个方格有4个三角形区域

        // 初始化并查集
        parent.resize(totalNodes);
        count = totalNodes;
        for (int i = 0; i < totalNodes; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i][j];

                // 连接当前方格内部的三角形区域
                if (c == ' ') {
                    // 空格：四个区域全部连通
                    unite(getIndex(n, i, j, 0), getIndex(n, i, j, 1));
                    unite(getIndex(n, i, j, 1), getIndex(n, i, j, 2));
                    unite(getIndex(n, i, j, 2), getIndex(n, i, j, 3));
                } else if (c == '/') {
                    // 左斜杠：上和左连通，右和下连通
                    unite(getIndex(n, i, j, 0), getIndex(n, i, j, 3));
                    unite(getIndex(n, i, j, 1), getIndex(n, i, j, 2));
                } else if (c == '\\') {
                    // 右斜杠：上和右连通，下和左连通
                    unite(getIndex(n, i, j, 0), getIndex(n, i, j, 1));
                    unite(getIndex(n, i, j, 2), getIndex(n, i, j, 3));
                }

                // 连接当前方格与下方方格
                if (i < n - 1) {
                    // 当前方格的下区域连接到下方方格的上区域
                    unite(getIndex(n, i, j, 2), getIndex(n, i + 1, j, 0));
                }

                // 连接当前方格与右方方格
                if (j < n - 1) {
                    // 当前方格的右区域连接到右方方格的左区域
                    unite(getIndex(n, i, j, 1), getIndex(n, i, j + 1, 3));
                }
            }
        }

        return count;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    RegionsCutBySlashes solution;

    // 测试用例1
    vector<string> grid1 = {
        " /",
        "/ "
    };
    cout << "测试用例1结果：" << solution.regionsBySlashes(grid1) << endl;
    // 预期输出：2

    // 测试用例2
    vector<string> grid2 = {
        " /",
        "  "
    };
    cout << "测试用例2结果：" << solution.regionsBySlashes(grid2) << endl;
    // 预期输出：1

    // 测试用例3
    vector<string> grid3 = {
        "\\/",
        "/\\"
    };
    cout << "测试用例3结果：" << solution.regionsBySlashes(grid3) << endl;
    // 预期输出：4

    // 测试用例4
    vector<string> grid4 = {
        "/\\",
        "\\/"
    };
    cout << "测试用例4结果：" << solution.regionsBySlashes(grid4) << endl;
    // 预期输出：5

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector容器存储并查集
 * 2. 使用const修饰不会修改的成员函数和参数
 * 3. 实现了简洁高效的路径压缩
 * 4. 使用unite命名替代union，避免与C++关键字冲突
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n²)
 * - 每个方格最多有常数次连接操作，每次连接的时间复杂度为O(α(n²))
 * - 总体时间复杂度：O(n² * α(n²)) ≈ O(n²)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n²)
 * - 总体空间复杂度：O(n²)
 * 
 * 代码设计思路：
 * 1. 将每个1x1方格分为四个三角形区域（0:上, 1:右, 2:下, 3:左）
 * 2. 根据方格中的字符，将内部区域连接起来
 * 3. 然后将相邻方格的对应区域连接起来
 * 4. 最终的连通分量数量即为区域数目
 */