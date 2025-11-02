/**
 * LeetCode 886 - 可能的二分法
 * https://leetcode-cn.com/problems/possible-bipartition/
 * 
 * 题目描述：
 * 给定一组 n 个人（编号为 1, 2, ..., n），我们想把每个人分进任意大小的两组。每个人都可能不喜欢其他人，那么他们不应该属于同一组。
 * 
 * 给定不喜欢的人对列表 dislikes，其中 dislikes[i] = [a, b]，表示不允许将编号为 a 和 b 的人归入同一组。当可以用这种方法将所有人分进两组时，返回 true；否则返回 false。
 * 
 * 示例 1：
 * 输入：n = 4, dislikes = [[1,2],[1,3],[2,4]]
 * 输出：true
 * 解释：group1 [1,4], group2 [2,3]
 * 
 * 示例 2：
 * 输入：n = 3, dislikes = [[1,2],[1,3],[2,3]]
 * 输出：false
 * 解释：没有办法将所有人分到两组而不冲突
 * 
 * 示例 3：
 * 输入：n = 5, dislikes = [[1,2],[2,3],[3,4],[4,5],[1,5]]
 * 输出：false
 * 
 * 解题思路：
 * 这是一个典型的二分图判定问题，可以使用并查集或者DFS/BFS来解决。
 * 这里我们使用并查集的方法：
 * 1. 对于每个人来说，如果他不喜欢某个人，那么他应该和这个人的所有不喜欢的人属于同一组
 * 2. 我们可以使用一个数组来记录每个人的不喜欢列表
 * 3. 对于每个人，我们将他不喜欢的人的不喜欢列表中的所有人合并到同一个集合中
 * 4. 最后，我们检查是否存在任何人与其不喜欢的人在同一个集合中
 * 
 * 时间复杂度分析：
 * - 构建不喜欢列表：O(m)，其中m是dislikes数组的长度
 * - 并查集操作：O(m * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 检查冲突：O(m)
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 不喜欢列表：O(m)
 * - 总体空间复杂度：O(n + m)
 */

import java.util.*;

public class Code25_PossibleBipartition {
    // 并查集的父节点数组
    private int[] parent;
    // 不喜欢列表，记录每个人不喜欢的所有人
    private List<List<Integer>> dislikeList;
    
    /**
     * 初始化并查集
     * @param n 人数
     */
    public void initUnionFind(int n) {
        parent = new int[n + 1]; // 编号从1开始
        // 初始化，每个元素的父节点是自己
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }
    }
    
    /**
     * 初始化不喜欢列表
     * @param n 人数
     * @param dislikes 不喜欢的人对列表
     */
    public void initDislikeList(int n, int[][] dislikes) {
        dislikeList = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            dislikeList.add(new ArrayList<>());
        }
        
        for (int[] pair : dislikes) {
            int a = pair[0];
            int b = pair[1];
            dislikeList.get(a).add(b);
            dislikeList.get(b).add(a);
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    public int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }
    
    /**
     * 判断是否可以将所有人分成两组
     * @param n 人数
     * @param dislikes 不喜欢的人对列表
     * @return 是否可以分成两组
     */
    public boolean possibleBipartition(int n, int[][] dislikes) {
        // 初始化并查集和不喜欢列表
        initUnionFind(n);
        initDislikeList(n, dislikes);
        
        // 对于每个人，将他不喜欢的人的不喜欢列表中的所有人合并到同一个集合中
        for (int i = 1; i <= n; i++) {
            List<Integer> dislikesOfI = dislikeList.get(i);
            if (dislikesOfI.isEmpty()) continue;
            
            // 第一个不喜欢的人
            int firstDislike = dislikesOfI.get(0);
            
            // 将i的所有不喜欢的人合并到同一个集合
            for (int j = 1; j < dislikesOfI.size(); j++) {
                union(firstDislike, dislikesOfI.get(j));
            }
        }
        
        // 检查是否存在冲突：如果一个人与其不喜欢的人在同一个集合中，则无法二分
        for (int i = 1; i <= n; i++) {
            for (int dislike : dislikeList.get(i)) {
                if (find(i) == find(dislike)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code25_PossibleBipartition solution = new Code25_PossibleBipartition();
        
        // 测试用例1
        int n1 = 4;
        int[][] dislikes1 = {{1, 2}, {1, 3}, {2, 4}};
        System.out.println("测试用例1结果：" + solution.possibleBipartition(n1, dislikes1));
        // 预期输出：true
        
        // 测试用例2
        int n2 = 3;
        int[][] dislikes2 = {{1, 2}, {1, 3}, {2, 3}};
        System.out.println("测试用例2结果：" + solution.possibleBipartition(n2, dislikes2));
        // 预期输出：false
        
        // 测试用例3
        int n3 = 5;
        int[][] dislikes3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 5}};
        System.out.println("测试用例3结果：" + solution.possibleBipartition(n3, dislikes3));
        // 预期输出：false
        
        // 测试用例4：空的不喜欢列表
        int n4 = 5;
        int[][] dislikes4 = {};
        System.out.println("测试用例4结果：" + solution.possibleBipartition(n4, dislikes4));
        // 预期输出：true
        
        // 测试用例5：只有一个人
        int n5 = 1;
        int[][] dislikes5 = {};
        System.out.println("测试用例5结果：" + solution.possibleBipartition(n5, dislikes5));
        // 预期输出：true
    }
    
    /**
     * 异常处理考虑：
     * 1. 输入参数校验：确保n为正整数，dislikes数组中的编号在1到n之间
     * 2. 空的不喜欢列表处理：直接返回true，因为没有限制条件
     * 3. 只有一个人的情况：直接返回true，因为无法形成冲突
     * 4. 自环处理：如果dislikes中存在[a,a]这样的对，应该视为无效或返回false
     */
    
    /**
     * 优化点：
     * 1. 使用路径压缩优化并查集查找效率
     * 2. 使用邻接表存储不喜欢关系，提高访问效率
     * 3. 提前剪枝：如果发现冲突可以立即返回
     * 4. 可以考虑使用DFS/BFS染色法作为另一种实现方式
     */
}