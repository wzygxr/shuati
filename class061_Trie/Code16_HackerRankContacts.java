package class045_Trie;

import java.util.List;
import java.util.ArrayList;

/**
 * HackerRank Contacts（联系人）
 * 
 * 题目描述：
 * 我们要制作自己的通讯录应用程序！该应用程序必须执行两种类型的操作：
 * 1. add name：添加联系人
 * 2. find partial：查找以指定前缀开头的联系人数量
 * 
 * 测试链接：https://www.hackerrank.com/challenges/contacts/problem
 * 
 * 算法思路：
 * 1. 使用前缀树存储所有联系人姓名
 * 2. 每个节点记录经过该节点的字符串数量
 * 3. 添加联系人时，沿路径增加计数
 * 4. 查找前缀时，返回前缀末尾节点的计数
 * 
 * 核心优化：
 * 在前缀树节点中维护经过计数，可以在O(L)时间内完成查找操作
 * 
 * 时间复杂度分析：
 * - 添加联系人：O(L)，其中L是姓名长度
 * - 查找前缀：O(L)，其中L是前缀长度
 * - 总体时间复杂度：O(N*L)，其中N是操作数量，L是平均字符串长度
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N*L)，用于存储所有联系人
 * - 总体空间复杂度：O(N*L)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地处理前缀查询操作
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或姓名为空的情况
 * 2. 边界情况：没有匹配联系人的情况
 * 3. 极端输入：大量联系人或姓名很长的情况
 * 4. 鲁棒性：处理重复姓名和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. HackerRank Contacts（联系人） (本题)
 * 2. LeetCode 208. 实现 Trie (前缀树)
 * 3. LeetCode 677. 键值映射
 * 4. 牛客网 NC141. 判断是否为回文字符串
 * 5. LintCode 442. 实现前缀树
 * 6. CodeChef - CONTACTS
 * 7. SPOJ - CONTACT
 * 8. AtCoder - Contact List
 */
public class Code16_HackerRankContacts {
    
    // 前缀树节点数量上限
    public static int MAXN = 2000000;
    
    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][26];
    
    // 经过每个节点的字符串数量
    public static int[] pass = new int[MAXN];
    
    // 当前使用的节点数量
    public static int cnt;
    
    /**
     * 初始化前缀树
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static void build() {
        cnt = 1;
    }
    
    /**
     * 将字符映射到路径索引
     * 
     * @param c 字符
     * @return 路径索引
     */
    public static int path(char c) {
        return c - 'a';
    }
    
    /**
     * 添加联系人
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历姓名的每个字符
     * 2. 对于每个字符，计算字符的路径索引
     * 3. 如果子节点不存在，则创建新节点
     * 4. 移动到子节点，增加通过计数
     * 5. 姓名遍历完成后，操作完成
     * 
     * 时间复杂度：O(L)，其中L是姓名长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * @param name 联系人姓名
     */
    public static void add(String name) {
        if (name == null || name.length() == 0) {
            return; // 空字符串不添加
        }
        
        int cur = 1;
        pass[cur]++;
        for (int i = 0; i < name.length(); i++) {
            int path = path(name.charAt(i));
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
            pass[cur]++;
        }
    }
    
    /**
     * 查找以指定前缀开头的联系人数量
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀的每个字符
     * 2. 对于每个字符，计算字符的路径索引
     * 3. 如果子节点不存在，说明没有匹配的联系人，返回0
     * 4. 移动到子节点，继续处理下一个字符
     * 5. 前缀遍历完成后，返回当前节点的通过计数
     * 
     * 时间复杂度：O(L)，其中L是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param partial 前缀
     * @return 匹配的联系人数量
     */
    public static int find(String partial) {
        if (partial == null || partial.length() == 0) {
            return pass[1]; // 空前缀匹配所有联系人
        }
        
        int cur = 1;
        for (int i = 0; i < partial.length(); i++) {
            int path = path(partial.charAt(i));
            if (tree[cur][path] == 0) {
                return 0;
            }
            cur = tree[cur][path];
        }
        return pass[cur];
    }
    
    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的通过计数重置为0
     * 
     * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
     * 空间复杂度：O(1)
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            for (int j = 0; j < 26; j++) {
                tree[i][j] = 0;
            }
            pass[i] = 0;
        }
    }
    
    /**
     * 处理联系人操作
     * 
     * 算法步骤：
     * 1. 初始化前缀树
     * 2. 遍历所有操作：
     *    a. 如果是add操作，调用add方法添加联系人
     *    b. 如果是find操作，调用find方法查找联系人数量
     * 3. 收集find操作的结果
     * 4. 清空前缀树
     * 
     * 时间复杂度：O(N*L)，其中N是操作数量，L是平均字符串长度
     * 空间复杂度：O(N*L)
     * 
     * @param operations 操作列表
     * @return find操作的结果列表
     */
    public static List<Integer> contacts(String[][] operations) {
        build();
        List<Integer> result = new ArrayList<>();
        
        for (String[] operation : operations) {
            String op = operation[0];
            String param = operation[1];
            
            if ("add".equals(op)) {
                add(param);
            } else if ("find".equals(op)) {
                result.add(find(param));
            }
        }
        
        clear();
        return result;
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 正常添加和查找：验证基本功能正确性
     * 2. 前缀匹配：验证前缀查询功能
     * 3. 重复姓名：验证重复处理
     * 4. 空输入处理：验证边界条件处理
     */
    public static void testContacts() {
        // 测试用例1：正常添加和查找
        String[][] operations1 = {
            {"add", "hack"},
            {"add", "hackerrank"},
            {"find", "hac"},
            {"find", "hak"}
        };
        List<Integer> result1 = contacts(operations1);
        List<Integer> expected1 = new ArrayList<>();
        expected1.add(2);
        expected1.add(0);
        assert result1.equals(expected1) : "测试用例1失败";
        
        // 测试用例2：重复姓名
        String[][] operations2 = {
            {"add", "s"},
            {"add", "ss"},
            {"add", "sss"},
            {"add", "ssss"},
            {"add", "sssss"},
            {"find", "s"},
            {"find", "ss"},
            {"find", "sss"}
        };
        List<Integer> result2 = contacts(operations2);
        List<Integer> expected2 = new ArrayList<>();
        expected2.add(5);
        expected2.add(4);
        expected2.add(3);
        assert result2.equals(expected2) : "测试用例2失败";
        
        // 测试用例3：空输入处理
        String[][] operations3 = {
            {"find", ""}
        };
        List<Integer> result3 = contacts(operations3);
        List<Integer> expected3 = new ArrayList<>();
        expected3.add(0);
        assert result3.equals(expected3) : "测试用例3失败";
        
        System.out.println("HackerRank Contacts 所有测试用例通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 添加大量联系人
     * 2. 执行大量查找操作
     */
    public static void performanceTest() {
        int n = 100000;
        String[][] operations = new String[n * 2][2];
        
        // 添加操作
        for (int i = 0; i < n; i++) {
            operations[i][0] = "add";
            operations[i][1] = "name" + i;
        }
        
        // 查找操作
        for (int i = 0; i < n; i++) {
            operations[n + i][0] = "find";
            operations[n + i][1] = "name";
        }
        
        long startTime = System.currentTimeMillis();
        List<Integer> result = contacts(operations);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理" + (n * 2) + "个操作耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果数量: " + result.size());
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testContacts();
        
        // 运行性能测试
        performanceTest();
    }
}