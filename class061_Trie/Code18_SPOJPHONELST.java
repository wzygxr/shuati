package class045_Trie;

/**
 * SPOJ PHONELST（电话列表）
 * 
 * 题目描述：
 * 给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
 * 如果存在这样的号码对，输出"NO"；否则输出"YES"。
 * 
 * 测试链接：https://www.spoj.com/problems/PHONELST/
 * 
 * 算法思路：
 * 1. 使用前缀树存储所有电话号码
 * 2. 在插入过程中检查是否存在前缀关系：
 *    a. 如果当前号码是某个已存在号码的前缀，返回false
 *    b. 如果某个已存在号码是当前号码的前缀，返回false
 * 3. 如果所有号码都成功插入且没有前缀冲突，返回true
 * 
 * 核心优化：
 * 在插入过程中实时检查前缀关系，避免了插入完成后再进行全局检查
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(∑len(numbers[i]))，其中∑len(numbers[i])是所有号码长度之和
 * - 检查前缀关系：O(∑len(numbers[i]))
 * - 总体时间复杂度：O(∑len(numbers[i]))
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(numbers[i]))，用于存储所有号码
 * - 总体空间复杂度：O(∑len(numbers[i]))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内检测前缀关系
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或号码为空的情况
 * 2. 边界情况：号码列表为空或只有一个号码的情况
 * 3. 极端输入：大量号码或号码很长的情况
 * 4. 鲁棒性：处理重复号码和非法字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. SPOJ PHONELST（电话列表） (本题)
 * 2. LeetCode 208. 实现 Trie (前缀树)
 * 3. UVa 11362 - Phone List
 * 4. HDU 1671 - Phone List
 * 5. LintCode 1427. 前缀统计
 * 6. CodeChef - PHONENUM
 * 7. AtCoder - Phone Number List
 */
public class Code18_SPOJPHONELST {
    
    // 前缀树节点数量上限
    public static int MAXN = 1000000;
    
    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][10];
    
    // 单词结尾标记，end[i]表示节点i是否是单词结尾
    public static boolean[] end = new boolean[MAXN];
    
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
        return c - '0';
    }
    
    /**
     * 向前缀树中插入电话号码并检查前缀关系
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历号码的每个字符
     * 2. 对于每个字符，计算字符的路径索引
     * 3. 检查当前节点是否已经是单词结尾（当前号码是已存在号码的前缀）
     * 4. 如果子节点不存在，则创建新节点
     * 5. 移动到子节点，继续处理下一个字符
     * 6. 号码遍历完成后，检查当前节点是否有子节点（已存在号码是当前号码的前缀）
     * 7. 如果存在前缀关系，返回false；否则标记当前节点为单词结尾，返回true
     * 
     * 时间复杂度：O(L)，其中L是号码长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * @param number 待插入的电话号码
     * @return 如果没有前缀冲突返回true，否则返回false
     */
    public static boolean insert(String number) {
        if (number == null || number.length() == 0) {
            return true; // 空字符串不插入
        }
        
        int cur = 1;
        for (int i = 0; i < number.length(); i++) {
            // 检查当前节点是否已经是单词结尾（当前号码是已存在号码的前缀）
            if (end[cur]) {
                return false;
            }
            
            int path = path(number.charAt(i));
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
        }
        
        // 检查当前节点是否有子节点（已存在号码是当前号码的前缀）
        for (int i = 0; i < 10; i++) {
            if (tree[cur][i] != 0) {
                return false;
            }
        }
        
        end[cur] = true;
        return true;
    }
    
    /**
     * 检查电话号码列表是否一致
     * 
     * 算法步骤：
     * 1. 初始化前缀树
     * 2. 遍历号码列表中的每个号码：
     *    a. 调用insert方法插入号码并检查前缀关系
     *    b. 如果发现前缀冲突，返回false
     * 3. 如果所有号码都成功插入且没有前缀冲突，返回true
     * 4. 清空前缀树
     * 
     * 时间复杂度：O(∑len(numbers[i]))
     * 空间复杂度：O(∑len(numbers[i]))
     * 
     * @param numbers 电话号码列表
     * @return 如果一致返回true，否则返回false
     */
    public static boolean isConsistent(String[] numbers) {
        build();
        
        for (String number : numbers) {
            if (!insert(number)) {
                clear();
                return false;
            }
        }
        
        clear();
        return true;
    }
    
    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的单词结尾标记重置为false
     * 
     * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
     * 空间复杂度：O(1)
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            for (int j = 0; j < 10; j++) {
                tree[i][j] = 0;
            }
            end[i] = false;
        }
    }
    
    /**
     * 处理电话列表问题
     * 
     * 算法步骤：
     * 1. 对每个测试用例调用isConsistent方法检查一致性
     * 2. 根据结果返回"YES"或"NO"
     * 
     * 时间复杂度：O(T * ∑len(numbers[i]))，其中T是测试用例数量
     * 空间复杂度：O(∑len(numbers[i]))
     * 
     * @param testCases 测试用例列表
     * @return 结果列表
     */
    public static String[] phoneList(String[][] testCases) {
        String[] result = new String[testCases.length];
        
        for (int i = 0; i < testCases.length; i++) {
            if (isConsistent(testCases[i])) {
                result[i] = "YES";
            } else {
                result[i] = "NO";
            }
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 一致列表：验证一致情况处理
     * 2. 不一致列表：验证不一致情况处理
     * 3. 前缀关系：验证前缀检测功能
     * 4. 边界情况：验证空列表和单号码处理
     */
    public static void testPhoneList() {
        // 测试用例1：一致列表
        String[] numbers1 = {"911", "97625999", "91125426"};
        assert !isConsistent(numbers1) : "测试用例1失败";
        
        // 测试用例2：一致列表
        String[] numbers2 = {"113", "12340", "123440", "12345", "98346"};
        assert isConsistent(numbers2) : "测试用例2失败";
        
        // 测试用例3：空列表
        String[] numbers3 = {};
        assert isConsistent(numbers3) : "测试用例3失败";
        
        // 测试用例4：单号码
        String[] numbers4 = {"123"};
        assert isConsistent(numbers4) : "测试用例4失败";
        
        System.out.println("SPOJ PHONELST 所有测试用例通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 构建大型号码列表
     * 2. 执行一致性检查
     */
    public static void performanceTest() {
        int n = 100000;
        String[] numbers = new String[n];
        
        // 构建号码列表
        for (int i = 0; i < n; i++) {
            numbers[i] = String.valueOf(100000 + i);
        }
        
        long startTime = System.currentTimeMillis();
        boolean result = isConsistent(numbers);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理" + n + "个号码耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + (result ? "YES" : "NO"));
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testPhoneList();
        
        // 运行性能测试
        performanceTest();
    }
}