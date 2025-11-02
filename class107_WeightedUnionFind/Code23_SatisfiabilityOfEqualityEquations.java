/**
 * LeetCode 990 - 等式方程的可满足性
 * https://leetcode-cn.com/problems/satisfiability-of-equality-equations/
 * 
 * 题目描述：
 * 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，并采用两种不同的形式之一："a==b" 或 "a!=b"。
 * 在这里，a 和 b 是小写字母（不一定不同），表示单字母变量名。
 * 
 * 只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。
 * 
 * 示例 1：
 * 输入：["a==b","b!=a"]
 * 输出：false
 * 解释：如果我们指定，a = 1 且 b = 1，那么可以满足第一个方程，但无法满足第二个方程。没有办法分配变量同时满足这两个方程。
 * 
 * 示例 2：
 * 输入：["b==a","a==b"]
 * 输出：true
 * 解释：我们可以指定 a = 1 且 b = 1 以满足满足这两个方程。
 * 
 * 示例 3：
 * 输入：["a==b","b==c","a==c"]
 * 输出：true
 * 
 * 解题思路：
 * 1. 使用并查集来处理等式关系（"a==b"）
 * 2. 首先处理所有的等式，将相等的变量合并到同一个集合中
 * 3. 然后处理所有的不等式（"a!=b"），检查a和b是否在同一个集合中
 * 4. 如果存在任何不等式的a和b在同一个集合中，则返回false
 * 5. 否则返回true
 * 
 * 时间复杂度分析：
 * - 处理所有等式：O(n * α(26))，其中n是方程的数量，α是阿克曼函数的反函数，近似为常数
 * - 处理所有不等式：O(n * α(26))
 * - 总体时间复杂度：O(n * α(26)) ≈ O(n)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(26)，因为变量名是小写字母
 * - 总体空间复杂度：O(1)
 */

public class Code23_SatisfiabilityOfEqualityEquations {
    // 并查集的父节点数组，26个小写字母
    private int[] parent;
    
    /**
     * 初始化并查集
     */
    public void initUnionFind() {
        parent = new int[26];
        // 初始化，每个元素的父节点是自己
        for (int i = 0; i < 26; i++) {
            parent[i] = i;
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素（0-25，对应a-z）
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
     * 判断等式方程是否可满足
     * @param equations 等式方程数组
     * @return 是否可满足
     */
    public boolean equationsPossible(String[] equations) {
        // 初始化并查集
        initUnionFind();
        
        // 第一遍：处理所有的等式（"a==b"）
        for (String equation : equations) {
            if (equation.charAt(1) == '=') { // 等式
                char var1 = equation.charAt(0);
                char var2 = equation.charAt(3);
                // 将相等的变量合并到同一个集合
                union(var1 - 'a', var2 - 'a');
            }
        }
        
        // 第二遍：处理所有的不等式（"a!=b"）
        for (String equation : equations) {
            if (equation.charAt(1) == '!') { // 不等式
                char var1 = equation.charAt(0);
                char var2 = equation.charAt(3);
                
                // 如果两个变量在同一个集合中，则违反不等式，返回false
                if (find(var1 - 'a') == find(var2 - 'a')) {
                    return false;
                }
            }
        }
        
        // 所有方程都满足
        return true;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code23_SatisfiabilityOfEqualityEquations solution = new Code23_SatisfiabilityOfEqualityEquations();
        
        // 测试用例1
        String[] equations1 = {"a==b", "b!=a"};
        System.out.println("测试用例1结果：" + solution.equationsPossible(equations1));
        // 预期输出：false
        
        // 测试用例2
        String[] equations2 = {"b==a", "a==b"};
        System.out.println("测试用例2结果：" + solution.equationsPossible(equations2));
        // 预期输出：true
        
        // 测试用例3
        String[] equations3 = {"a==b", "b==c", "a==c"};
        System.out.println("测试用例3结果：" + solution.equationsPossible(equations3));
        // 预期输出：true
        
        // 测试用例4
        String[] equations4 = {"a==b", "b!=c", "c==a"};
        System.out.println("测试用例4结果：" + solution.equationsPossible(equations4));
        // 预期输出：false
        
        // 测试用例5
        String[] equations5 = {"c==c", "b==d", "x!=z"};
        System.out.println("测试用例5结果：" + solution.equationsPossible(equations5));
        // 预期输出：true
        
        // 测试用例6：单个变量的等式和不等式
        String[] equations6 = {"a==a", "a!=a"};
        System.out.println("测试用例6结果：" + solution.equationsPossible(equations6));
        // 预期输出：false
    }
    
    /**
     * 异常处理考虑：
     * 1. 输入参数校验：确保equations数组中的每个字符串都是有效的等式
     * 2. 自反性处理：a==a总是成立的，a!=a总是不成立的
     * 3. 传递性处理：a==b和b==c意味着a==c
     * 4. 反对称性处理：a!=b意味着b!=a
     * 
     * 注意事项：
     * 1. 由于变量名只能是小写字母，所以并查集的大小固定为26
     * 2. 必须先处理所有等式，再处理不等式，否则会出现逻辑错误
     * 3. 不需要按秩合并，因为集合大小很小（最多26个元素）
     */
    
    /**
     * 优化点：
     * 1. 使用路径压缩优化并查集查找效率
     * 2. 由于变量数量固定为26个，空间复杂度为O(1)
     * 3. 两次遍历方程数组，避免了复杂的排序操作
     */
}