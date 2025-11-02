/**
 * LeetCode 721 - 账户合并
 * https://leetcode-cn.com/problems/accounts-merge/
 * 
 * 题目描述：
 * 给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，其中第一个元素 accounts[i][0] 是名称 (name)，其余元素是 emails 表示该账户的邮箱地址。
 * 
 * 现在，我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
 * 
 * 合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。账户本身可以以任意顺序返回。
 * 
 * 示例 1：
 * 输入：
 * accounts = [
 *   ["John", "johnsmith@mail.com", "john00@mail.com"],
 *   ["John", "johnnybravo@mail.com"],
 *   ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
 *   ["Mary", "mary@mail.com"]
 * ]
 * 输出：
 * [
 *   ["John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"],
 *   ["John", "johnnybravo@mail.com"],
 *   ["Mary", "mary@mail.com"]
 * ]
 * 解释：
 * 第一个和第三个 John 是同一个人，因为他们有共同的邮箱 "johnsmith@mail.com"。
 * 合并后，他们的邮箱是 ["john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"]。
 * 第二个 John 和 Mary 是不同的人，因为他们的邮箱没有交集。
 * 
 * 解题思路：
 * 1. 使用并查集来合并具有共同邮箱的账户
 * 2. 首先为每个唯一邮箱分配一个唯一ID，并记录邮箱与账户名称的映射关系
 * 3. 对于每个账户，将该账户中的所有邮箱合并到同一个集合中
 * 4. 最后，将同一集合中的邮箱按照账户名称分组，并排序
 * 
 * 时间复杂度分析：
 * - 初始化并处理邮箱：O(n * m)，其中n是账户数量，m是平均每个账户的邮箱数量
 * - 合并操作：O(n * m * α(k))，其中k是唯一邮箱的数量，α是阿克曼函数的反函数，近似为常数
 * - 排序邮箱：O(k log k)，其中k是唯一邮箱的数量
 * - 总体时间复杂度：O(n * m + k log k)
 * 
 * 空间复杂度分析：
 * - 存储邮箱ID和映射关系：O(k)
 * - 并查集数组：O(k)
 * - 存储结果：O(k)
 * - 总体空间复杂度：O(k)
 */

import java.util.*;

public class Code24_AccountsMerge {
    // 并查集的父节点数组
    private int[] parent;
    
    /**
     * 初始化并查集
     * @param size 元素数量
     */
    public void initUnionFind(int size) {
        parent = new int[size];
        // 初始化，每个元素的父节点是自己
        for (int i = 0; i < size; i++) {
            parent[i] = i;
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
     * 合并账户
     * @param accounts 账户列表
     * @return 合并后的账户列表
     */
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        // 1. 为每个唯一邮箱分配一个唯一ID，并记录邮箱与账户名称的映射关系
        Map<String, Integer> emailToId = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();
        int emailId = 0;
        
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                if (!emailToId.containsKey(email)) {
                    emailToId.put(email, emailId++);
                    emailToName.put(email, name);
                }
            }
        }
        
        // 2. 初始化并查集
        initUnionFind(emailId);
        
        // 3. 对于每个账户，将该账户中的所有邮箱合并到同一个集合中
        for (List<String> account : accounts) {
            if (account.size() > 1) { // 确保账户至少有一个邮箱
                String firstEmail = account.get(1);
                int firstId = emailToId.get(firstEmail);
                
                // 将当前账户的所有其他邮箱与第一个邮箱合并
                for (int i = 2; i < account.size(); i++) {
                    String currentEmail = account.get(i);
                    int currentId = emailToId.get(currentEmail);
                    union(firstId, currentId);
                }
            }
        }
        
        // 4. 收集每个集合中的邮箱
        Map<Integer, List<String>> idToEmails = new HashMap<>();
        for (String email : emailToId.keySet()) {
            int emailIdValue = emailToId.get(email);
            int rootId = find(emailIdValue);
            
            idToEmails.putIfAbsent(rootId, new ArrayList<>());
            idToEmails.get(rootId).add(email);
        }
        
        // 5. 构建结果
        List<List<String>> result = new ArrayList<>();
        for (List<String> emails : idToEmails.values()) {
            // 排序邮箱
            Collections.sort(emails);
            
            // 创建账户记录
            List<String> account = new ArrayList<>();
            // 添加名称（可以从任意一个邮箱获取）
            account.add(emailToName.get(emails.get(0)));
            // 添加排序后的邮箱
            account.addAll(emails);
            
            result.add(account);
        }
        
        return result;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code24_AccountsMerge solution = new Code24_AccountsMerge();
        
        // 测试用例1
        List<List<String>> accounts1 = Arrays.asList(
            Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"),
            Arrays.asList("John", "johnnybravo@mail.com"),
            Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"),
            Arrays.asList("Mary", "mary@mail.com")
        );
        
        List<List<String>> result1 = solution.accountsMerge(accounts1);
        System.out.println("测试用例1结果：");
        for (List<String> account : result1) {
            System.out.println(account);
        }
        
        // 测试用例2：只有一个账户
        List<List<String>> accounts2 = Arrays.asList(
            Arrays.asList("Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe1@m.co")
        );
        
        List<List<String>> result2 = solution.accountsMerge(accounts2);
        System.out.println("测试用例2结果：");
        for (List<String> account : result2) {
            System.out.println(account);
        }
        
        // 测试用例3：没有账户
        List<List<String>> accounts3 = new ArrayList<>();
        List<List<String>> result3 = solution.accountsMerge(accounts3);
        System.out.println("测试用例3结果：" + result3);
    }
    
    /**
     * 异常处理考虑：
     * 1. 空账户列表处理：直接返回空列表
     * 2. 账户格式验证：确保每个账户至少包含名称
     * 3. 重复邮箱处理：通过Map自动去重
     * 4. 邮箱排序：确保结果中的邮箱按ASCII顺序排列
     */
    
    /**
     * 优化点：
     * 1. 使用路径压缩优化并查集查找效率
     * 2. 使用HashMap高效管理邮箱ID和名称映射
     * 3. 按需创建数据结构，减少内存占用
     * 4. 对邮箱进行排序，满足题目要求
     */
}