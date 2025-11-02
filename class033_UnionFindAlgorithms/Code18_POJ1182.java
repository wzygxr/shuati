package class056;

import java.util.*;

/**
 * POJ 1182 食物链
 * 链接: http://poj.org/problem?id=1182
 * 难度: 中等
 * 
 * 题目描述:
 * 动物王国中有三类动物A,B,C，这三类动物的食物链构成了有趣的环形。A吃B， B吃C，C吃A。
 * 现有N个动物，以1－N编号。每个动物都是A,B,C中的一种，但是我们并不知道它到底是哪一种。
 * 有人用两种说法对这N个动物所构成的食物链关系进行描述：
 * 第一种说法是"1 X Y"，表示X和Y是同类。
 * 第二种说法是"2 X Y"，表示X吃Y。
 * 此人对N个动物，用上述两种说法，一句接一句地说出K句话，这K句话有的是真的，有的是假的。
 * 当一句话满足下列三条之一时，这句话就是假话，否则就是真话。
 * 1） 当前的话与前面的某些真的话冲突，就是假话；
 * 2） 当前的话中X或Y比N大，就是假话；
 * 3） 当前的话表示X吃X，就是假话。
 * 你的任务是根据给定的N（1 <= N <= 50,000）和K句话（0 <= K <= 100,000），输出假话的总数。
 * 
 * 输入格式:
 * 第一行是两个整数N和K，以一个空格分隔。
 * 以下K行每行是三个正整数 D，X，Y，两数之间用一个空格隔开，其中D表示说法的种类。
 * 若D=1，则表示X和Y是同类。
 * 若D=2，则表示X吃Y。
 * 
 * 输出格式:
 * 只有一个整数，表示假话的数目。
 * 
 * 示例输入:
 * 100 7
 * 1 101 1 
 * 2 1 2
 * 2 2 3 
 * 2 3 3 
 * 1 1 3 
 * 2 3 1 
 * 1 5 5
 * 
 * 示例输出:
 * 3
 */
public class Code18_POJ1182 {
    
    /**
     * 使用带权并查集解决食物链问题
     * 时间复杂度: O(K * α(N))，其中K是语句数量，N是动物数量
     * 空间复杂度: O(N)
     * 
     * 解题思路:
     * 1. 使用带权并查集，每个元素存储到父节点的关系（0表示同类，1表示吃父节点，2表示被父节点吃）
     * 2. 关系满足模3运算的传递性:
     *    - 如果A和B同类，B和C同类，则A和C同类 (0+0=0 mod 3)
     *    - 如果A吃B，B吃C，则A被C吃 (1+1=2 mod 3)
     *    - 如果A吃B，B被C吃，则A和C同类 (1+2=0 mod 3)
     * 3. 对于每个语句，检查是否与已知关系冲突
     */
    public int findInvalidStatements(int n, int[][] statements) {
        // 初始化并查集
        int[] parent = new int[n + 1]; // 动物编号从1开始
        int[] relation = new int[n + 1]; // relation[i]表示i到父节点的关系
        
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            relation[i] = 0; // 初始时每个节点的父节点是自己，关系为同类
        }
        
        int invalidCount = 0;
        
        for (int[] statement : statements) {
            int type = statement[0];
            int x = statement[1];
            int y = statement[2];
            
            // 检查条件2：X或Y比N大
            if (x > n || y > n) {
                invalidCount++;
                continue;
            }
            
            // 检查条件3：X吃X
            if (type == 2 && x == y) {
                invalidCount++;
                continue;
            }
            
            int rootX = find(x, parent, relation);
            int rootY = find(y, parent, relation);
            
            if (rootX == rootY) {
                // X和Y已经在同一集合中，检查是否冲突
                // 计算X到Y的关系：relation[x] - relation[y] mod 3
                int relationXToY = (relation[x] - relation[y] + 3) % 3;
                
                if (type == 1 && relationXToY != 0) {
                    // 声明X和Y是同类，但实际不是
                    invalidCount++;
                } else if (type == 2 && relationXToY != 1) {
                    // 声明X吃Y，但实际不是
                    invalidCount++;
                }
            } else {
                // 合并两个集合
                // 将rootY合并到rootX
                parent[rootY] = rootX;
                
                // 计算rootY到rootX的关系
                // 期望关系: X和Y的关系为type-1 (0表示同类，1表示吃)
                // 根据关系传递性: relation[x] + relation[rootY] ≡ (type-1) + relation[y] (mod 3)
                // 所以: relation[rootY] ≡ (type-1) + relation[y] - relation[x] (mod 3)
                relation[rootY] = (type - 1 + relation[y] - relation[x] + 3) % 3;
            }
        }
        
        return invalidCount;
    }
    
    /**
     * 查找根节点并进行路径压缩，同时更新关系
     */
    private int find(int x, int[] parent, int[] relation) {
        if (parent[x] != x) {
            int originalParent = parent[x];
            parent[x] = find(parent[x], parent, relation);
            // 更新关系：x到新根节点的关系 = x到原父节点的关系 + 原父节点到新根节点的关系
            relation[x] = (relation[x] + relation[originalParent]) % 3;
        }
        return parent[x];
    }
    
    /**
     * 方法2: 使用扩展域并查集（三倍空间法）解决食物链问题
     * 时间复杂度: O(K * α(3N)) ≈ O(K * α(N))
     * 空间复杂度: O(3N)
     * 
     * 解题思路:
     * 1. 将每个动物拆分成三个域：A域、B域、C域
     * 2. 如果X和Y是同类，则合并(X_A, Y_A), (X_B, Y_B), (X_C, Y_C)
     * 3. 如果X吃Y，则合并(X_A, Y_B), (X_B, Y_C), (X_C, Y_A)
     * 4. 检查冲突: 如果声明X和Y同类，但X_A和Y_B或Y_C在同一集合，则冲突
     *             如果声明X吃Y，但X_A和Y_A或Y_C在同一集合，则冲突
     */
    public int findInvalidStatementsExtendedDomain(int n, int[][] statements) {
        // 扩展域并查集，大小为3n
        int[] parent = new int[3 * n + 1];
        
        for (int i = 1; i <= 3 * n; i++) {
            parent[i] = i;
        }
        
        int invalidCount = 0;
        
        for (int[] statement : statements) {
            int type = statement[0];
            int x = statement[1];
            int y = statement[2];
            
            // 检查条件2：X或Y比N大
            if (x > n || y > n) {
                invalidCount++;
                continue;
            }
            
            // 检查条件3：X吃X
            if (type == 2 && x == y) {
                invalidCount++;
                continue;
            }
            
            if (type == 1) {
                // 声明X和Y是同类
                if (find(x, parent) == find(y + n, parent) || find(x, parent) == find(y + 2 * n, parent)) {
                    // 如果X和Y_B或Y_C在同一集合，说明X吃Y或被Y吃，冲突
                    invalidCount++;
                } else {
                    // 合并三个域
                    union(x, y, parent);
                    union(x + n, y + n, parent);
                    union(x + 2 * n, y + 2 * n, parent);
                }
            } else {
                // 声明X吃Y
                if (find(x, parent) == find(y, parent) || find(x, parent) == find(y + 2 * n, parent)) {
                    // 如果X和Y是同类，或者Y吃X，冲突
                    invalidCount++;
                } else {
                    // 合并对应域
                    union(x, y + n, parent);        // X_A和Y_B合并
                    union(x + n, y + 2 * n, parent); // X_B和Y_C合并
                    union(x + 2 * n, y, parent);     // X_C和Y_A合并
                }
            }
        }
        
        return invalidCount;
    }
    
    /**
     * 标准并查集查找操作
     */
    private int find(int x, int[] parent) {
        if (parent[x] != x) {
            parent[x] = find(parent[x], parent);
        }
        return parent[x];
    }
    
    /**
     * 标准并查集合并操作
     */
    private void union(int x, int y, int[] parent) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }
    
    /**
     * 方法3: 使用更清晰的带权并查集实现
     */
    static class FoodChainUnionFind {
        private int[] parent;
        private int[] relation; // 0: 同类, 1: 吃父节点, 2: 被父节点吃
        private int invalidCount;
        
        public FoodChainUnionFind(int n) {
            parent = new int[n + 1];
            relation = new int[n + 1];
            invalidCount = 0;
            
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                relation[i] = 0;
            }
        }
        
        public int processStatements(int[][] statements) {
            for (int[] statement : statements) {
                int type = statement[0];
                int x = statement[1];
                int y = statement[2];
                
                processStatement(type, x, y);
            }
            return invalidCount;
        }
        
        private void processStatement(int type, int x, int y) {
            // 边界条件检查
            if (x > parent.length - 1 || y > parent.length - 1) {
                invalidCount++;
                return;
            }
            
            if (type == 2 && x == y) {
                invalidCount++;
                return;
            }
            
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                // 已经在同一集合，检查关系是否冲突
                int relationXY = (relation[x] - relation[y] + 3) % 3;
                
                if (type == 1 && relationXY != 0) {
                    invalidCount++;
                } else if (type == 2 && relationXY != 1) {
                    invalidCount++;
                }
            } else {
                // 合并集合
                parent[rootY] = rootX;
                relation[rootY] = (type - 1 + relation[y] - relation[x] + 3) % 3;
            }
        }
        
        private int find(int x) {
            if (parent[x] != x) {
                int originalParent = parent[x];
                parent[x] = find(parent[x]);
                relation[x] = (relation[x] + relation[originalParent]) % 3;
            }
            return parent[x];
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code18_POJ1182 solution = new Code18_POJ1182();
        
        // 测试用例1: 示例输入
        int n1 = 100;
        int[][] statements1 = {
            {1, 101, 1},  // 假话：101 > 100
            {2, 1, 2},     // 真话
            {2, 2, 3},     // 真话
            {2, 3, 3},     // 假话：3吃3
            {1, 1, 3},     // 假话：根据前面，1吃2，2吃3，所以1被3吃，不是同类
            {2, 3, 1},     // 真话：3吃1（符合环形关系）
            {1, 5, 5}      // 真话：5和5是同类
        };
        
        int result1 = solution.findInvalidStatements(n1, statements1);
        System.out.println("测试用例1 - 带权并查集: " + result1); // 预期: 3
        
        int result1Extended = solution.findInvalidStatementsExtendedDomain(n1, statements1);
        System.out.println("测试用例1 - 扩展域: " + result1Extended); // 预期: 3
        
        FoodChainUnionFind uf1 = new FoodChainUnionFind(n1);
        int result1Class = uf1.processStatements(statements1);
        System.out.println("测试用例1 - 类封装: " + result1Class); // 预期: 3
        
        // 测试用例2: 简单测试
        int n2 = 5;
        int[][] statements2 = {
            {1, 1, 2},     // 真话：1和2是同类
            {2, 2, 3},     // 真话：2吃3
            {2, 3, 4},     // 真话：3吃4
            {1, 1, 4}      // 假话：1和4应该是吃的关系（1->2->3->4，所以1被4吃）
        };
        
        int result2 = solution.findInvalidStatements(n2, statements2);
        System.out.println("测试用例2 - 带权并查集: " + result2); // 预期: 1
        
        // 测试用例3: 环形关系测试
        int n3 = 3;
        int[][] statements3 = {
            {2, 1, 2},     // 真话：1吃2
            {2, 2, 3},     // 真话：2吃3
            {2, 3, 1}      // 真话：3吃1（形成环形）
        };
        
        int result3 = solution.findInvalidStatements(n3, statements3);
        System.out.println("测试用例3 - 带权并查集: " + result3); // 预期: 0
    }
    
    /**
     * 食物链问题总结:
     * 
     * 1. 带权并查集解法（推荐）:
     *    - 优点: 空间效率高，只需要O(N)空间
     *    - 缺点: 关系计算需要模运算，理解稍复杂
     *    - 适用: 大多数场景
     * 
     * 2. 扩展域并查集解法:
     *    - 优点: 逻辑清晰，易于理解
     *    - 缺点: 需要3倍空间，空间效率较低
     *    - 适用: 当关系种类较多时（如更多种类的关系）
     * 
     * 3. 关键技巧:
     *    - 关系传递性: 使用模运算处理环形关系
     *    - 路径压缩: 同时更新关系值
     *    - 边界处理: 注意动物编号范围和自相矛盾的情况
     * 
     * 4. 应用扩展:
     *    - 可以扩展到更多种类的关系（如4种、5种动物）
     *    - 可以处理更复杂的关系网络
     */
}