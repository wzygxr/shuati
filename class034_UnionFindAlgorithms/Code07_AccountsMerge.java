package class057;

import java.util.*;

/**
 * 账户合并 (Accounts Merge)
 * 给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，
 * 其中第一个元素 accounts[i][0] 是 名称 (name)，其余元素是 emails 表示该账户的邮箱地址。
 * 现在我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。
 * 请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。
 * 一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
 * 合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。
 * 账户本身可以以任意顺序返回。
 * 
 * 示例 1:
 * 输入：
 * [["John", "johnsmith@mail.com", "john00@mail.com"],
 *  ["John", "johnnybravo@mail.com"],
 *  ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
 *  ["Mary", "mary@mail.com"]]
 * 输出：
 * [["John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"],
 *  ["John", "johnnybravo@mail.com"],
 *  ["Mary", "mary@mail.com"]]
 * 
 * 示例 2:
 * 输入：
 * [["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],
 *  ["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],
 *  ["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],
 *  ["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],
 *  ["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]]
 * 输出：
 * [["Ethan","Ethan0@m.co","Ethan4@m.co","Ethan5@m.co"],
 *  ["Gabe","Gabe0@m.co","Gabe1@m.co","Gabe3@m.co"],
 *  ["Hanzo","Hanzo0@m.co","Hanzo1@m.co","Hanzo3@m.co"],
 *  ["Kevin","Kevin0@m.co","Kevin3@m.co","Kevin5@m.co"],
 *  ["Fern","Fern0@m.co","Fern1@m.co","Fern5@m.co"]]
 * 
 * 测试链接: https://leetcode.cn/problems/accounts-merge/
 * 
 * 算法思路深度解析:
 * 1. 问题的核心是识别哪些账户属于同一个人 - 即具有共享邮箱的账户应该被合并
 * 2. 使用并查集来高效管理账户之间的连通性关系
 * 3. 算法步骤：
 *    a. 建立邮箱到账户索引的映射关系
 *    b. 对于每个邮箱，如果它已经出现在之前的账户中，就将当前账户与之前的账户合并
 *    c. 建立根账户索引到邮箱集合的映射，收集同一连通分量中的所有邮箱
 *    d. 按照要求格式化结果，确保邮箱按字典序排列
 * 4. 这个问题中，账户是并查集的节点，邮箱是连接节点的边
 * 
 * 算法性能分析:
 * 时间复杂度: O(n*m*α(n) + n*m*log(n*m))，其中：
 *             - n是账户数量，m是每个账户平均邮箱数量
 *             - α是阿克曼函数的反函数，在实际应用中接近常数级别
 *             - log(n*m)来自TreeSet的排序操作
 * 空间复杂度: O(n*m)，用于存储邮箱映射和结果
 * 
 * 是否为最优解: 是，该解法在时间和空间复杂度上都是最优的
 * 
 * 工程化考量:
 * 1. 异常处理：对空输入和无效输入进行充分检查
 * 2. 数据结构选择：使用HashMap快速查找，TreeSet保持邮箱排序
 * 3. 模块化设计：将并查集封装为独立类，提高代码可读性
 * 4. 扩展性：可以轻松扩展以支持其他合并规则或数据类型
 * 5. 性能优化：实现了路径压缩和按秩合并两种关键优化
 * 
 * 与其他领域的联系:
 * 1. 数据清洗：在数据挖掘和数据分析中，常需要合并重复或相关的数据记录
 * 2. 社交网络分析：识别同一用户的多个账户或身份
 * 3. 数据库设计：解决实体识别问题，将不同表示形式的同一实体关联起来
 * 4. 身份认证：在安全系统中处理多个身份标识符的关联
 * 5. 推荐系统：确保为同一用户提供一致的推荐体验
 * 
 * 极端情况分析:
 * 1. 空账户列表：正确返回空列表
 * 2. 单个账户：直接返回该账户
 * 3. 所有账户共享同一个邮箱：所有账户合并为一个
 * 4. 每个账户只有名称没有邮箱：处理边界情况
 * 5. 大量账户和邮箱：算法仍保持高效
 */
public class Code07_AccountsMerge {
    
    /**
     * 并查集(Union-Find)类的实现
     * 支持快速查找和合并操作，使用路径压缩和按秩合并优化
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界，用于优化合并操作
        
        /**
         * 初始化并查集
         * @param n 节点数量
         * @throws IllegalArgumentException 如果节点数量小于0
         */
        public UnionFind(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("节点数量不能为负数: " + n);
            }
            
            parent = new int[n];
            rank = new int[n];
            
            // 初始化：每个节点的父节点是自己，秩为1
            for (int i = 0; i < n; i++) {
                parent[i] = i;    // 自环
                rank[i] = 1;      // 初始树高度
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化，使得后续查找操作接近O(1)
         * 
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         * @throws IndexOutOfBoundsException 如果x超出有效范围
         */
        public int find(int x) {
            // 参数有效性检查
            if (x < 0 || x >= parent.length) {
                throw new IndexOutOfBoundsException("节点索引超出范围: " + x);
            }
            
            // 路径压缩：将查找路径上的所有节点直接连接到根节点
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 递归查找根节点并更新父节点引用
            }
            return parent[x];
        }
        
        /**
         * 合并两个节点所在的集合
         * 使用按秩合并优化，避免树高度过深
         * 
         * @param x 第一个节点
         * @param y 第二个节点
         * @throws IndexOutOfBoundsException 如果x或y超出有效范围
         */
        public void union(int x, int y) {
            // 找到两个节点的根节点
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一个集合中，无需合并
            if (rootX == rootY) {
                return;
            }
            
            // 按秩合并：将秩小的树合并到秩大的树下
            if (rank[rootX] > rank[rootY]) {
                // x的树更高，将y的树连接到x的树
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                // y的树更高，将x的树连接到y的树
                parent[rootX] = rootY;
            } else {
                // 两棵树高度相同，任选一棵作为根，并增加其秩
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
        
        /**
         * 判断两个节点是否在同一个集合中
         * 
         * @param x 第一个节点
         * @param y 第二个节点
         * @return 如果两个节点在同一个集合中返回true，否则返回false
         * @throws IndexOutOfBoundsException 如果x或y超出有效范围
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    /**
     * 合并账户的核心方法
     * 使用并查集将具有共同邮箱的账户合并
     * 
     * @param accounts 账户列表，每个账户是一个字符串列表，第一个元素是名称，其余是邮箱
     * @return 合并后的账户列表，每个账户的邮箱按字典序排列
     * @throws IllegalArgumentException 如果账户列表或其中的账户数据无效
     */
    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        // 参数有效性检查
        if (accounts == null) {
            throw new IllegalArgumentException("账户列表不能为null");
        }
        
        // 边界条件检查
        if (accounts.isEmpty()) {
            return new ArrayList<>();
        }
        
        int n = accounts.size();
        
        // 创建并查集，每个账户对应一个节点
        UnionFind uf = new UnionFind(n);
        
        // 建立邮箱到账户索引的映射，用于快速查找邮箱所属的账户
        Map<String, Integer> emailToIndex = new HashMap<>();
        
        // 第一步：建立账户之间的连接关系
        // 遍历所有账户
        for (int i = 0; i < n; i++) {
            List<String> account = accounts.get(i);
            
            // 检查账户是否有效
            if (account == null || account.isEmpty()) {
                continue;
            }
            
            // 验证账户至少包含名称
            if (account.size() < 1) {
                throw new IllegalArgumentException("账户必须至少包含名称");
            }
            
            // 从第二个元素开始是邮箱地址
            for (int j = 1; j < account.size(); j++) {
                String email = account.get(j);
                
                // 验证邮箱不为null
                if (email == null) {
                    throw new IllegalArgumentException("邮箱地址不能为null");
                }
                
                // 如果邮箱已经出现过，将当前账户与之前出现该邮箱的账户连接
                if (emailToIndex.containsKey(email)) {
                    uf.union(i, emailToIndex.get(email));
                } else {
                    // 记录邮箱对应的账户索引
                    emailToIndex.put(email, i);
                }
            }
        }
        
        // 第二步：将同一连通分量中的所有邮箱合并到一起
        // 建立根账户索引到邮箱集合的映射
        Map<Integer, TreeSet<String>> indexToEmails = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            // 找到当前账户所属集合的根节点
            int root = uf.find(i);
            
            // 如果根节点还没有对应的邮箱集合，创建一个
            if (!indexToEmails.containsKey(root)) {
                indexToEmails.put(root, new TreeSet<>());
            }
            
            // 将当前账户的所有邮箱添加到根节点对应的集合中
            List<String> account = accounts.get(i);
            for (int j = 1; j < account.size(); j++) {
                indexToEmails.get(root).add(account.get(j));
            }
        }
        
        // 第三步：构造结果列表
        List<List<String>> result = new ArrayList<>();
        
        for (Map.Entry<Integer, TreeSet<String>> entry : indexToEmails.entrySet()) {
            int rootIndex = entry.getKey();
            TreeSet<String> emails = entry.getValue();
            
            // 创建新的账户列表
            List<String> account = new ArrayList<>();
            // 添加名称（使用根节点对应的账户名称）
            account.add(accounts.get(rootIndex).get(0));
            // 添加排序后的邮箱（TreeSet已经维护了字典序）
            account.addAll(emails);
            
            // 添加到结果列表
            result.add(account);
        }
        
        return result;
    }
    
    /**
     * 主测试方法
     * 运行所有测试用例，验证算法的正确性和鲁棒性
     */
    public static void main(String[] args) {
        runBasicTests();
        runBoundaryTests();
        runSpecialTests();
        runExceptionTests();
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 运行基本功能测试用例
     */
    private static void runBasicTests() {
        System.out.println("========== 基本功能测试 ==========");
        
        // 测试用例1：包含需要合并的账户
        testCase1();
        
        // 测试用例2：多个独立账户
        testCase2();
    }
    
    /**
     * 运行边界情况测试用例
     */
    private static void runBoundaryTests() {
        System.out.println("\n========== 边界情况测试 ==========");
        
        // 测试用例3：单个账户
        testCase3();
        
        // 测试用例4：空列表
        testCase4();
        
        // 测试用例5：只有名称没有邮箱的账户
        testCase5();
        
        // 测试用例6：单个账户多个邮箱
        testCase6();
    }
    
    /**
     * 运行特殊情况测试用例
     */
    private static void runSpecialTests() {
        System.out.println("\n========== 特殊情况测试 ==========");
        
        // 测试用例7：所有账户都需要合并
        testCase7();
        
        // 测试用例8：相同名称不同账户
        testCase8();
    }
    
    /**
     * 运行异常处理测试用例
     */
    private static void runExceptionTests() {
        System.out.println("\n========== 异常处理测试 ==========");
        
        // 测试用例9：无效输入（null）
        testCase9();
    }
    
    /**
     * 测试用例1：包含需要合并的账户
     */
    private static void testCase1() {
        System.out.println("\n测试用例1：包含需要合并的账户");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"));
        accounts.add(Arrays.asList("John", "johnnybravo@mail.com"));
        accounts.add(Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"));
        accounts.add(Arrays.asList("Mary", "mary@mail.com"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例2：多个独立账户
     */
    private static void testCase2() {
        System.out.println("\n测试用例2：多个独立账户");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"));
        accounts.add(Arrays.asList("Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"));
        accounts.add(Arrays.asList("Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"));
        accounts.add(Arrays.asList("Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"));
        accounts.add(Arrays.asList("Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例3：单个账户
     */
    private static void testCase3() {
        System.out.println("\n测试用例3：单个账户");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("Alice", "alice@example.com"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例4：空列表
     */
    private static void testCase4() {
        System.out.println("\n测试用例4：空列表");
        List<List<String>> accounts = new ArrayList<>();
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果: " + result);
    }
    
    /**
     * 测试用例5：只有名称没有邮箱的账户
     */
    private static void testCase5() {
        System.out.println("\n测试用例5：只有名称没有邮箱的账户");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("Bob"));
        accounts.add(Arrays.asList("Charlie"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例6：单个账户多个邮箱
     */
    private static void testCase6() {
        System.out.println("\n测试用例6：单个账户多个邮箱");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("David", "david1@mail.com", "david2@mail.com", "david3@mail.com"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例7：所有账户都需要合并
     */
    private static void testCase7() {
        System.out.println("\n测试用例7：所有账户都需要合并");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("Eve", "eve1@mail.com", "eve2@mail.com"));
        accounts.add(Arrays.asList("Eve", "eve2@mail.com", "eve3@mail.com"));
        accounts.add(Arrays.asList("Eve", "eve3@mail.com", "eve4@mail.com"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例8：相同名称不同账户
     */
    private static void testCase8() {
        System.out.println("\n测试用例8：相同名称不同账户");
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(Arrays.asList("Adam", "adam1@mail.com"));
        accounts.add(Arrays.asList("Adam", "adam2@mail.com"));
        accounts.add(Arrays.asList("Adam", "adam3@mail.com"));
        
        List<List<String>> result = accountsMerge(accounts);
        System.out.println("结果:");
        for (List<String> account : result) {
            System.out.println(account);
        }
    }
    
    /**
     * 测试用例9：异常处理 - null输入
     */
    private static void testCase9() {
        System.out.println("\n测试用例9：异常处理 - null输入");
        try {
            accountsMerge(null);
            System.out.println("测试失败：应该抛出IllegalArgumentException异常");
        } catch (IllegalArgumentException e) {
            System.out.println("测试成功：正确捕获异常 - " + e.getMessage());
        }
    }
}

/* C++ 实现
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <set>
#include <algorithm>
#include <stdexcept>
using namespace std;

/**
 * 并查集(Union-Find)类的实现
 * 支持快速查找和合并操作，使用路径压缩和按秩合并优化
 */
class UnionFind {
private:
    vector<int> parent;  // 父节点数组
    vector<int> rank;    // 秩数组，用于优化合并操作

public:
    /**
     * 初始化并查集
     * @param n 节点数量
     * @throws invalid_argument 如果节点数量小于0
     */
    UnionFind(int n) {
        if (n < 0) {
            throw invalid_argument("节点数量不能为负数: " + to_string(n));
        }
        
        parent.resize(n);
        rank.resize(n, 1);
        
        // 初始化：每个节点的父节点是自己
        for (int i = 0; i < n; ++i) {
            parent[i] = i;
        }
    }
    
    /**
     * 查找节点的根节点（带路径压缩）
     * @param x 要查找的节点
     * @return 根节点
     * @throws out_of_range 如果x超出有效范围
     */
    int find(int x) {
        // 参数有效性检查
        if (x < 0 || x >= parent.size()) {
            throw out_of_range("节点索引超出范围: " + to_string(x));
        }
        
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // 路径压缩
        }
        return parent[x];
    }
    
    /**
     * 合并两个集合（按秩合并）
     * @param x 第一个节点
     * @param y 第二个节点
     * @throws out_of_range 如果x或y超出有效范围
     */
    void unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return;  // 已经在同一个集合中
        }
        
        // 按秩合并
        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
    
    /**
     * 判断两个节点是否在同一个集合中
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 如果两个节点在同一个集合中返回true，否则返回false
     * @throws out_of_range 如果x或y超出有效范围
     */
    bool isConnected(int x, int y) {
        return find(x) == find(y);
    }
};

/**
 * 账户合并问题解决方案
 * 使用并查集高效管理账户之间的连通性
 */
class Solution {
public:
    /**
     * 合并账户
     * @param accounts 账户列表
     * @return 合并后的账户列表
     * @throws invalid_argument 如果账户列表或其中的账户数据无效
     */
    vector<vector<string>> accountsMerge(vector<vector<string>>& accounts) {
        // 边界条件检查
        if (accounts.empty()) {
            return {};
        }
        
        int n = accounts.size();
        
        // 创建并查集
        UnionFind uf(n);
        
        // 建立邮箱到账户索引的映射
        unordered_map<string, int> emailToIndex;
        
        // 遍历所有账户，建立连接关系
        for (int i = 0; i < n; ++i) {
            const vector<string>& account = accounts[i];
            
            // 检查账户是否有效
            if (account.empty()) {
                continue;
            }
            
            // 验证账户至少包含名称
            if (account.size() < 1) {
                throw invalid_argument("账户必须至少包含名称");
            }
            
            // 从第二个元素开始是邮箱地址
            for (int j = 1; j < account.size(); ++j) {
                const string& email = account[j];
                
                // 验证邮箱不为空
                if (email.empty()) {
                    throw invalid_argument("邮箱地址不能为空");
                }
                
                // 如果邮箱已经出现过，合并账户
                if (emailToIndex.find(email) != emailToIndex.end()) {
                    uf.unite(i, emailToIndex[email]);
                } else {
                    // 记录邮箱对应的账户索引
                    emailToIndex[email] = i;
                }
            }
        }
        
        // 将同一连通分量中的所有邮箱合并
        unordered_map<int, set<string>> indexToEmails;
        
        for (int i = 0; i < n; ++i) {
            int root = uf.find(i);
            
            if (indexToEmails.find(root) == indexToEmails.end()) {
                indexToEmails[root] = set<string>();
            }
            
            const vector<string>& account = accounts[i];
            for (int j = 1; j < account.size(); ++j) {
                indexToEmails[root].insert(account[j]);
            }
        }
        
        // 构造结果
        vector<vector<string>> result;
        
        for (const auto& entry : indexToEmails) {
            int rootIndex = entry.first;
            const set<string>& emails = entry.second;
            
            vector<string> account;
            account.push_back(accounts[rootIndex][0]);  // 添加名称
            account.insert(account.end(), emails.begin(), emails.end());  // 添加排序后的邮箱
            
            result.push_back(account);
        }
        
        return result;
    }
};

/**
 * 打印结果辅助函数
 * @param result 要打印的账户列表
 */
void printResult(const vector<vector<string>>& result) {
    for (const auto& account : result) {
        cout << "[";
        for (size_t i = 0; i < account.size(); ++i) {
            cout << "\"" << account[i] << "\"";
            if (i < account.size() - 1) {
                cout << ", ";
            }
        }
        cout << "]" << endl;
    }
}

/**
 * 运行基本功能测试用例
 */
void runBasicTests() {
    cout << "========== 基本功能测试 ==========" << endl;
    Solution solution;
    
    // 测试用例1：包含需要合并的账户
    cout << "\n测试用例1：包含需要合并的账户" << endl;
    vector<vector<string>> accounts1 = {
        {"John", "johnsmith@mail.com", "john00@mail.com"},
        {"John", "johnnybravo@mail.com"},
        {"John", "johnsmith@mail.com", "john_newyork@mail.com"},
        {"Mary", "mary@mail.com"}
    };
    vector<vector<string>> result1 = solution.accountsMerge(accounts1);
    printResult(result1);
    
    // 测试用例2：多个独立账户
    cout << "\n测试用例2：多个独立账户" << endl;
    vector<vector<string>> accounts2 = {
        {"Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"},
        {"Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"},
        {"Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"},
        {"Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"},
        {"Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"}
    };
    vector<vector<string>> result2 = solution.accountsMerge(accounts2);
    printResult(result2);
}

/**
 * 运行边界情况测试用例
 */
void runBoundaryTests() {
    cout << "\n========== 边界情况测试 ==========" << endl;
    Solution solution;
    
    // 测试用例3：单个账户
    cout << "\n测试用例3：单个账户" << endl;
    vector<vector<string>> accounts3 = {"Alice", "alice@example.com"};
    vector<vector<string>> result3 = solution.accountsMerge(accounts3);
    printResult(result3);
    
    // 测试用例4：空列表
    cout << "\n测试用例4：空列表" << endl;
    vector<vector<string>> accounts4 = {};
    vector<vector<string>> result4 = solution.accountsMerge(accounts4);
    printResult(result4);
}

int main() {
    // 运行所有测试用例
    runBasicTests();
    runBoundaryTests();
    
    cout << "\n所有测试用例执行完成！" << endl;
    return 0;
}
*/

/* Python 实现
class UnionFind:
    """
    并查集类，用于高效处理元素的合并和查询
    实现了路径压缩和按秩合并优化
    """
    
    def __init__(self, n):
        """
        初始化并查集
        
        Args:
            n: 节点数量
        
        Raises:
            ValueError: 如果节点数量小于0
        """
        if n < 0:
            raise ValueError(f"节点数量不能为负数: {n}")
        
        # 初始化父节点数组，每个节点初始指向自己
        self.parent = list(range(n))
        # 初始化秩数组，用于按秩合并优化
        self.rank = [1] * n
    
    def find(self, x):
        """
        查找节点的根节点（代表元素）
        使用路径压缩优化
        
        Args:
            x: 要查找的节点
            
        Returns:
            节点所在集合的根节点
            
        Raises:
            IndexError: 如果x超出有效范围
        """
        # 参数有效性检查
        if x < 0 or x >= len(self.parent):
            raise IndexError(f"节点索引超出范围: {x}")
        
        if self.parent[x] != x:
            # 路径压缩：将查找路径上的所有节点直接连接到根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个节点所在的集合
        使用按秩合并优化
        
        Args:
            x: 第一个节点
            y: 第二个节点
            
        Raises:
            IndexError: 如果x或y超出有效范围
        """
        # 找到两个节点的根节点
        root_x = self.find(x)
        root_y = self.find(y)
        
        # 如果已经在同一个集合中，无需合并
        if root_x == root_y:
            return
        
        # 按秩合并：将秩小的树合并到秩大的树下
        if self.rank[root_x] > self.rank[root_y]:
            # x的树更高，将y的树连接到x的树
            self.parent[root_y] = root_x
        elif self.rank[root_x] < self.rank[root_y]:
            # y的树更高，将x的树连接到y的树
            self.parent[root_x] = root_y
        else:
            # 两棵树高度相同，任选一棵作为根，并增加其秩
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
    
    def is_connected(self, x, y):
        """
        判断两个节点是否在同一个集合中
        
        Args:
            x: 第一个节点
            y: 第二个节点
            
        Returns:
            bool: 如果两个节点在同一个集合中返回True，否则返回False
            
        Raises:
            IndexError: 如果x或y超出有效范围
        """
        return self.find(x) == self.find(y)

class Solution:
    """
    账户合并问题解决方案
    使用并查集高效管理账户之间的连通性
    """
    
    def accountsMerge(self, accounts):
        """
        合并具有共同邮箱的账户
        
        Args:
            accounts: 账户列表，每个账户是一个列表，第一个元素是名称，其余是邮箱
            
        Returns:
            list: 合并后的账户列表，每个账户的邮箱按字典序排列
            
        Raises:
            ValueError: 如果账户列表或其中的账户数据无效
        """
        # 参数有效性检查
        if accounts is None:
            raise ValueError("账户列表不能为None")
        
        # 边界条件检查
        if not accounts:
            return []
        
        n = len(accounts)
        
        # 创建并查集，每个账户对应一个节点
        uf = UnionFind(n)
        
        # 建立邮箱到账户索引的映射，用于快速查找邮箱所属的账户
        email_to_index = {}
        
        # 第一步：建立账户之间的连接关系
        for i, account in enumerate(accounts):
            # 检查账户是否有效
            if account is None or not account:
                continue
            
            # 验证账户至少包含名称
            if len(account) < 1:
                raise ValueError("账户必须至少包含名称")
            
            # 从第二个元素开始是邮箱地址
            for email in account[1:]:
                # 验证邮箱不为None
                if email is None:
                    raise ValueError("邮箱地址不能为None")
                
                # 如果邮箱已经出现过，将当前账户与之前出现该邮箱的账户连接
                if email in email_to_index:
                    uf.union(i, email_to_index[email])
                else:
                    # 记录邮箱对应的账户索引
                    email_to_index[email] = i
        
        # 第二步：将同一连通分量中的所有邮箱合并到一起
        # 建立根账户索引到邮箱集合的映射
        index_to_emails = {}
        
        for i in range(n):
            # 找到当前账户所属集合的根节点
            root = uf.find(i)
            
            # 如果根节点还没有对应的邮箱集合，创建一个
            if root not in index_to_emails:
                index_to_emails[root] = set()
            
            # 将当前账户的所有邮箱添加到根节点对应的集合中
            for email in accounts[i][1:]:
                index_to_emails[root].add(email)
        
        # 第三步：构造结果列表
        result = []
        
        for root_index, emails in index_to_emails.items():
            # 创建新的账户列表
            account = [accounts[root_index][0]]  # 添加名称
            # 添加排序后的邮箱
            account.extend(sorted(emails))
            # 添加到结果列表
            result.append(account)
        
        return result

# 测试代码
def print_result(result):
    """打印结果辅助函数"""
    for account in result:
        print(account)

def test_solution():
    """运行所有测试用例"""
    solution = Solution()
    
    # 测试用例1：包含需要合并的账户
    accounts1 = [
        ["John", "johnsmith@mail.com", "john00@mail.com"],
        ["John", "johnnybravo@mail.com"],
        ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
        ["Mary", "mary@mail.com"]
    ]
    result1 = solution.accountsMerge(accounts1)
    print("测试用例1结果:")
    print_result(result1)
    
    # 测试用例2：多个独立账户
    accounts2 = [
        ["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],
        ["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],
        ["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],
        ["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],
        ["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]
    ]
    result2 = solution.accountsMerge(accounts2)
    print("\n测试用例2结果:")
    print_result(result2)
    
    # 测试用例3：边界情况 - 单个账户
    accounts3 = [["Alice", "alice@example.com"]]
    result3 = solution.accountsMerge(accounts3)
    print("\n测试用例3结果:")
    print_result(result3)
    
    # 测试用例4：边界情况 - 空列表
    accounts4 = []
    result4 = solution.accountsMerge(accounts4)
    print("\n测试用例4结果:")
    print(result4)

def run_basic_tests():
    """运行基本功能测试用例"""
    print("========== 基本功能测试 ==========")
    solution = Solution()
    
    # 测试用例1：包含需要合并的账户
    print("\n测试用例1：包含需要合并的账户")
    accounts1 = [
        ["John", "johnsmith@mail.com", "john00@mail.com"],
        ["John", "johnnybravo@mail.com"],
        ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
        ["Mary", "mary@mail.com"]
    ]
    result1 = solution.accountsMerge(accounts1)
    print_result(result1)
    
    # 测试用例2：多个独立账户
    print("\n测试用例2：多个独立账户")
    accounts2 = [
        ["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],
        ["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],
        ["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],
        ["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],
        ["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]
    ]
    result2 = solution.accountsMerge(accounts2)
    print_result(result2)

def run_boundary_tests():
    """运行边界情况测试用例"""
    print("\n========== 边界情况测试 ==========")
    solution = Solution()
    
    # 测试用例3：单个账户
    print("\n测试用例3：单个账户")
    accounts3 = [["Alice", "alice@example.com"]]
    result3 = solution.accountsMerge(accounts3)
    print_result(result3)
    
    # 测试用例4：空列表
    print("\n测试用例4：空列表")
    accounts4 = []
    result4 = solution.accountsMerge(accounts4)
    print(result4)
    
    # 测试用例5：只有名称没有邮箱的账户
    print("\n测试用例5：只有名称没有邮箱的账户")
    accounts5 = [["Bob"], ["Charlie"]]
    result5 = solution.accountsMerge(accounts5)
    print_result(result5)

def run_special_tests():
    """运行特殊情况测试用例"""
    print("\n========== 特殊情况测试 ==========")
    solution = Solution()
    
    # 测试用例6：所有账户都需要合并
    print("\n测试用例6：所有账户都需要合并")
    accounts6 = [
        ["Eve", "eve1@mail.com", "eve2@mail.com"],
        ["Eve", "eve2@mail.com", "eve3@mail.com"],
        ["Eve", "eve3@mail.com", "eve4@mail.com"]
    ]
    result6 = solution.accountsMerge(accounts6)
    print_result(result6)

def run_exception_tests():
    """运行异常处理测试用例"""
    print("\n========== 异常处理测试 ==========")
    solution = Solution()
    
    # 测试用例7：异常处理 - None输入
    print("\n测试用例7：异常处理 - None输入")
    try:
        solution.accountsMerge(None)
        print("测试失败：应该抛出ValueError异常")
    except ValueError as e:
        print(f"测试成功：正确捕获异常 - {e}")

# 执行所有测试
if __name__ == "__main__":
    run_basic_tests()
    run_boundary_tests()
    run_special_tests()
    run_exception_tests()
    print("\n所有测试用例执行完成！")
*/
*/