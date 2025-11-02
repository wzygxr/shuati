package class045;

import java.util.*;

/**
 * LeetCode 1268. 搜索推荐系统
 * 
 * 题目描述：
 * 给你一个产品数组 products 和一个字符串 searchWord，products 数组中每个产品都是一个字符串。
 * 请你设计一个推荐系统，在依次输入单词 searchWord 的每一个字母后，推荐 products 数组中前缀与当前输入的字母相同的最多三个产品。
 * 如果前缀相同的可推荐产品超过三个，请按字典序返回最小的三个。
 * 请你以二维列表的形式，返回在输入 searchWord 每个字母后相应的推荐产品的列表。
 *
 * 示例：
 * 输入：products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
 * 输出：[
 *   ["mobile","moneypot","monitor"],
 *   ["mobile","moneypot","monitor"],
 *   ["mouse","mousepad"],
 *   ["mouse","mousepad"],
 *   ["mouse","mousepad"]
 * ]
 * 
 * 解释：按字典序排序后的产品列表是 ["mobile","moneypot","monitor","mouse","mousepad"]
 * 输入 m 和 mo，由于所有产品的前缀都相同，所以系统返回字典序最小的三个产品 ["mobile","moneypot","monitor"]
 * 输入 mou， mous 和 mouse 后系统都返回 ["mouse","mousepad"]
 *
 * 测试链接：https://leetcode.cn/problems/search-suggestions-system/
 * 
 * 算法思路：
 * 1. 前缀树 + 深度优先搜索：为每个前缀收集最多3个产品
 * 2. 构建前缀树存储所有产品名称
 * 3. 对于搜索词的每个前缀，在前缀树中查找并收集推荐产品
 * 4. 使用深度优先搜索按字典序收集产品
 *
 * 核心优化思路：
 * 1. 使用静态数组实现前缀树，提高访问效率
 * 2. 在构建前缀树前对产品列表进行排序，确保DFS收集的产品自然按字典序排列
 * 3. 提供两种实现方案：
 *    - 前缀树方案：构建Trie树，适合频繁查询场景
 *    - 排序+二分查找方案：简单直接，适合一次性查询场景
 *
 * 算法步骤详解：
 * 1. 前缀树方案：
 *    - 对产品列表按字典序排序
 *    - 构建前缀树，将所有产品插入到Trie中
 *    - 对于搜索词的每个前缀（从第一个字符到第i个字符），在前缀树中查找对应的节点
 *    - 从该节点开始进行深度优先搜索，按字典序收集最多3个产品
 *    - 将收集到的产品作为当前前缀的推荐结果
 *
 * 2. 排序+二分查找方案：
 *    - 对产品列表按字典序排序
 *    - 对于搜索词的每个前缀，使用二分查找找到第一个匹配的产品
 *    - 从该位置开始顺序收集最多3个匹配产品
 *
 * 时间复杂度分析：
 * - 前缀树方案：
 *   - 构建前缀树：O(∑len(products[i]))，其中∑len(products[i])是所有产品名称长度之和
 *   - 查询过程：O(∑len(searchWord) + K)，其中K是结果总长度
 *   - 总体时间复杂度：O(∑len(products[i]) + ∑len(searchWord) + K)
 * - 排序+二分查找方案：
 *   - 排序：O(N * M * log N)，其中N是产品数量，M是平均产品名称长度
 *   - 查询：O(M * (log N + L))，其中M是搜索词长度，L是平均推荐产品数量
 *   - 总体时间复杂度：O(N * M * log N + M * (log N + L))
 *
 * 空间复杂度分析：
 * - 前缀树方案：
 *   - 前缀树空间：O(∑len(products[i]) * Σ)，其中Σ是字符集大小（此处为26）
 *   - 结果空间：O(∑len(searchWord) * 3)
 *   - 总体空间复杂度：O(∑len(products[i]) * Σ + ∑len(searchWord))
 * - 排序+二分查找方案：
 *   - 排序空间：O(N * M)
 *   - 结果空间：O(∑len(searchWord) * 3)
 *   - 总体空间复杂度：O(N * M + ∑len(searchWord))
 *
 * 是否最优解：是
 * 理由：使用前缀树可以高效处理前缀相关的搜索推荐，相比暴力搜索方法效率更高
 *
 * 工程化考虑：
 * 1. 异常处理：处理空产品列表和空搜索词，防止空指针异常
 * 2. 边界情况：产品数量不足3个的情况，需正确处理
 * 3. 极端输入：大量产品或长产品名称的情况，需考虑内存使用和性能
 * 4. 内存管理：合理管理前缀树内存，使用完后及时清理
 * 5. 可扩展性：支持不同的字符集，不仅限于小写字母
 *
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，性能较高；对象引用机制简化内存管理
 * C++：可使用指针实现，更节省空间；需要手动管理内存释放
 * Python：可使用字典实现，代码更简洁；动态类型提供灵活性但可能牺牲性能
 *
 * 调试技巧：
 * 1. 验证每个前缀的推荐结果是否符合字典序要求
 * 2. 测试边界情况（产品数量不足3个、空前缀等）
 * 3. 单元测试覆盖各种场景，包括重复产品名称
 * 4. 性能测试对比不同实现方案的效率差异
 *
 * 相关题目扩展：
 * 1. LeetCode 208. 实现 Trie (前缀树) - 前缀树的基础实现
 * 2. LeetCode 211. 添加与搜索单词 - 数据结构设计 - 支持通配符匹配的前缀树
 * 3. LeetCode 212. 单词搜索 II - 结合DFS和前缀树的应用
 * 4. LeetCode 642. 设计搜索自动补全系统 - 搜索推荐系统的变体
 * 5. LeetCode 648. 单词替换 - 前缀树在字符串替换中的应用
 */
public class Code12_LeetCode1268 {
    
    /**
     * 主方法：生成搜索推荐
     * 使用前缀树方案实现搜索推荐系统
     * 
     * 算法步骤：
     * 1. 对产品列表进行排序（按字典序）
     * 2. 构建前缀树，插入所有产品名称
     * 3. 对于搜索词的每个前缀，查找推荐产品
     * 4. 使用深度优先搜索收集最多3个产品
     * 
     * @param products 产品列表 - 非空字符串数组
     * @param searchWord 搜索词 - 非空字符串
     * @return 每个前缀的推荐产品列表 - 二维列表，外层列表长度等于搜索词长度
     */
    public static List<List<String>> suggestedProducts(String[] products, String searchWord) {
        // 初始化结果列表
        List<List<String>> result = new ArrayList<>();
        
        // 参数校验：检查输入是否为空
        if (products == null || products.length == 0 || searchWord == null || searchWord.length() == 0) {
            // 处理空输入：为搜索词的每个字符创建一个空的推荐列表
            for (int i = 0; i < searchWord.length(); i++) {
                result.add(new ArrayList<>());
            }
            return result;
        }
        
        // 对产品列表排序（按字典序）
        // 这样可以确保在DFS收集产品时自然按字典序排列
        Arrays.sort(products);
        
        // 构建前缀树，将所有产品插入到Trie中
        build(products);
        
        // 为搜索词的每个前缀生成推荐
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < searchWord.length(); i++) {
            // 逐个添加字符构建前缀
            prefix.append(searchWord.charAt(i));
            // 获取当前前缀的推荐产品（最多3个）
            List<String> suggestions = getSuggestions(prefix.toString(), 3);
            // 将推荐结果添加到结果列表
            result.add(suggestions);
        }
        
        // 清空前缀树，释放内存
        clear();
        return result;
    }
    
    /**
     * 获取指定前缀的推荐产品
     * 
     * 算法步骤：
     * 1. 在前缀树中查找前缀对应的节点
     * 2. 从该节点开始深度优先搜索，收集最多limit个产品
     * 3. 按字典序返回推荐产品
     * 
     * @param prefix 搜索前缀 - 非空字符串
     * @param limit 最大推荐数量 - 正整数
     * @return 推荐产品列表 - 按字典序排列，数量不超过limit
     */
    private static List<String> getSuggestions(String prefix, int limit) {
        // 初始化推荐结果列表
        List<String> suggestions = new ArrayList<>();
        
        // 查找前缀对应的节点
        int node = findPrefixNode(prefix);
        if (node == 0) {
            return suggestions; // 前缀不存在，返回空列表
        }
        
        // 深度优先搜索收集产品
        // 使用StringBuilder构建当前路径，避免频繁字符串拼接
        dfs(node, new StringBuilder(prefix), suggestions, limit);
        
        return suggestions;
    }
    
    /**
     * 深度优先搜索收集产品
     * 按字典序遍历子节点，确保收集的产品自然按字典序排列
     * 
     * 算法步骤：
     * 1. 如果当前节点是产品结尾，添加到结果列表
     * 2. 如果结果数量达到限制，提前返回
     * 3. 按字典序遍历子节点（a-z顺序）
     * 4. 递归搜索子节点
     * 
     * @param node 当前节点编号 - 正整数
     * @param current 当前路径字符串 - 用于构建完整产品名称
     * @param result 结果列表 - 存储收集到的产品名称
     * @param limit 最大数量限制 - 正整数
     */
    private static void dfs(int node, StringBuilder current, List<String> result, int limit) {
        // 如果结果数量达到限制，提前返回以提高效率
        if (result.size() >= limit) {
            return;
        }
        
        // 如果当前节点是产品结尾（存储了产品名称），添加到结果
        if (end[node] != null) {
            result.add(current.toString());
        }
        
        // 按字典序遍历子节点（a-z顺序）
        // 由于产品列表已排序，DFS自然按字典序收集产品
        for (int i = 0; i < 26; i++) {
            // 如果子节点存在
            if (tree[node][i] != 0) {
                // 添加当前字符到路径
                current.append((char) ('a' + i));
                // 递归搜索子节点
                dfs(tree[node][i], current, result, limit);
                // 回溯：移除当前字符
                current.deleteCharAt(current.length() - 1);
                
                // 如果结果数量达到限制，提前返回以提高效率
                if (result.size() >= limit) {
                    return;
                }
            }
        }
    }
    
    /**
     * 查找前缀对应的节点
     * 在前缀树中查找指定前缀的末尾节点
     * 
     * @param prefix 前缀 - 非空字符串
     * @return 节点编号，如果前缀不存在返回0
     */
    private static int findPrefixNode(String prefix) {
        // 从前缀树根节点开始（编号为1）
        int cur = 1;
        // 遍历前缀中的每个字符
        for (int i = 0; i < prefix.length(); i++) {
            // 计算字符相对于'a'的索引
            int index = prefix.charAt(i) - 'a';
            // 检查索引有效性并查找子节点
            if (index < 0 || index >= 26 || tree[cur][index] == 0) {
                return 0; // 字符无效或子节点不存在，前缀不存在
            }
            // 移动到子节点
            cur = tree[cur][index];
        }
        // 返回前缀末尾节点的编号
        return cur;
    }
    
    // 前缀树相关变量
    private static final int MAXN = 100000; // 最大节点数，根据实际需求调整
    private static int[][] tree = new int[MAXN][26]; // 前缀树结构，tree[node][char]表示节点node的字符char对应的子节点编号
    private static String[] end = new String[MAXN]; // 存储以该节点结尾的产品名称，null表示非结尾节点
    private static int cnt; // 当前使用的节点编号计数器
    
    /**
     * 构建前缀树
     * 将所有产品插入到前缀树中
     * 
     * @param products 产品列表 - 非空字符串数组
     */
    private static void build(String[] products) {
        // 重置节点计数器，从根节点（编号1）开始
        cnt = 1;
        // 遍历所有产品
        for (String product : products) {
            // 将产品插入前缀树
            insert(product);
        }
    }
    
    /**
     * 向前缀树中插入产品名称
     * 
     * @param product 产品名称 - 非空字符串
     */
    private static void insert(String product) {
        // 从前缀树根节点开始（编号1）
        int cur = 1;
        // 遍历产品名称中的每个字符
        for (int i = 0; i < product.length(); i++) {
            // 计算字符相对于'a'的索引
            int index = product.charAt(i) - 'a';
            // 检查字符有效性（只处理小写字母）
            if (index < 0 || index >= 26) {
                continue; // 跳过非法字符
            }
            
            // 如果子节点不存在，则分配新节点
            if (tree[cur][index] == 0) {
                tree[cur][index] = ++cnt; // 分配新节点编号并建立连接
            }
            // 移动到子节点
            cur = tree[cur][index];
        }
        // 在末尾节点存储产品名称
        end[cur] = product;
    }
    
    /**
     * 清空前缀树
     * 释放前缀树占用的内存，避免内存泄漏
     */
    private static void clear() {
        // 遍历所有已使用的节点
        for (int i = 1; i <= cnt; i++) {
            // 清空子节点指针数组
            Arrays.fill(tree[i], 0);
            // 清空产品名称引用
            end[i] = null;
        }
    }
    
    /**
     * 简化版本：使用排序和二分查找
     * 适合一次性查询场景，实现简单但查询效率较低
     * 
     * 算法思路：
     * 1. 对产品列表排序
     * 2. 对于每个前缀，使用二分查找找到第一个匹配的产品
     * 3. 收集最多3个匹配产品
     * 
     * 时间复杂度：O(n log n + m * n)，其中n是产品数量，m是搜索词长度
     * 空间复杂度：O(n)
     */
    public static List<List<String>> suggestedProductsSimplified(String[] products, String searchWord) {
        // 初始化结果列表
        List<List<String>> result = new ArrayList<>();
        
        // 参数校验：检查输入是否为空
        if (products == null || products.length == 0 || searchWord == null) {
            return result;
        }
        
        // 对产品列表排序，确保结果按字典序排列
        Arrays.sort(products);
        
        // 对于搜索词的每个前缀（从第一个字符到第i个字符）
        for (int i = 1; i <= searchWord.length(); i++) {
            // 提取当前前缀
            String prefix = searchWord.substring(0, i);
            // 初始化当前前缀的推荐列表
            List<String> suggestions = new ArrayList<>();
            
            // 使用二分查找找到第一个匹配的产品
            // left指向第一个可能匹配的位置
            int left = 0, right = products.length;
            while (left < right) {
                int mid = left + (right - left) / 2;
                // 比较中间产品与前缀的字典序
                if (products[mid].compareTo(prefix) < 0) {
                    // 中间产品小于前缀，需要向右查找
                    left = mid + 1;
                } else {
                    // 中间产品大于等于前缀，可能匹配，向左查找
                    right = mid;
                }
            }
            
            // 从找到的位置开始收集最多3个匹配产品
            for (int j = left; j < Math.min(left + 3, products.length); j++) {
                // 检查产品是否以当前前缀开头
                if (products[j].startsWith(prefix)) {
                    suggestions.add(products[j]);
                } else {
                    // 由于产品已排序，后续产品也不会匹配，可以提前退出
                    break;
                }
            }
            
            // 将当前前缀的推荐结果添加到结果列表
            result.add(suggestions);
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     * 验证搜索推荐系统的正确性和边界情况处理
     */
    public static void testSuggestedProducts() {
        // 测试用例1：基础功能测试
        String[] products1 = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
        String searchWord1 = "mouse";
        List<List<String>> result1 = suggestedProducts(products1, searchWord1);
        
        // 验证结果
        assert result1.size() == 5 : "结果数量不正确";
        assert result1.get(0).size() == 3 : "第一个前缀推荐数量不正确";
        assert result1.get(0).contains("mobile") : "推荐产品不正确";
        
        // 测试用例2：产品数量不足3个
        String[] products2 = {"havana"};
        String searchWord2 = "havana";
        List<List<String>> result2 = suggestedProducts(products2, searchWord2);
        
        assert result2.size() == 6 : "havana测试结果数量不正确";
        for (int i = 0; i < 6; i++) {
            assert result2.get(i).size() == 1 : "havana测试推荐数量不正确";
            assert result2.get(i).get(0).equals("havana") : "推荐产品不正确";
        }
        
        // 测试用例3：空输入
        String[] products3 = {};
        String searchWord3 = "test";
        List<List<String>> result3 = suggestedProducts(products3, searchWord3);
        
        assert result3.size() == 4 : "空输入测试结果数量不正确";
        for (List<String> list : result3) {
            assert list.isEmpty() : "空输入测试推荐应该为空";
        }
        
        // 测试简化版本
        List<List<String>> result1Simplified = suggestedProductsSimplified(products1, searchWord1);
        assert result1Simplified.size() == result1.size() : "简化版本结果数量不一致";
        
        System.out.println("所有单元测试通过！");
    }
    
    /**
     * 性能测试方法
     * 对比前缀树方案和简化方案的性能差异
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 10000;
        String[] products = new String[n];
        String searchWord = "test";
        
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            // 生成随机产品名称（长度5-10）
            int length = 5 + random.nextInt(6);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < length; j++) {
                sb.append((char) ('a' + random.nextInt(26)));
            }
            products[i] = sb.toString();
        }
        
        // 前缀树版本性能测试
        long startTime = System.currentTimeMillis();
        List<List<String>> result1 = suggestedProducts(products, searchWord);
        long trieTime = System.currentTimeMillis() - startTime;
        
        // 简化版本性能测试
        startTime = System.currentTimeMillis();
        List<List<String>> result2 = suggestedProductsSimplified(products, searchWord);
        long simplifiedTime = System.currentTimeMillis() - startTime;
        
        System.out.println("前缀树版本耗时: " + trieTime + "ms");
        System.out.println("简化版本耗时: " + simplifiedTime + "ms");
        System.out.println("处理了 " + n + " 个产品和搜索词 '" + searchWord + "'");
        
        // 验证结果一致性（小规模测试）
        if (n <= 100) {
            assert result1.size() == result2.size() : "结果数量不一致";
            for (int i = 0; i < result1.size(); i++) {
                assert result1.get(i).equals(result2.get(i)) : "推荐结果不一致";
            }
            System.out.println("结果验证通过！");
        }
    }
    
    /**
     * 主函数：程序入口点
     * 执行各种测试以验证实现的正确性和性能
     */
    public static void main(String[] args) {
        // 运行单元测试
        testSuggestedProducts();
        
        // 运行性能测试
        performanceTest();
    }
}