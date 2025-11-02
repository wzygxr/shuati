package class056;

import java.util.*;

/**
 * 句子相似性II
 * 给定两个句子 sentence1 和 sentence2 分别表示为一个字符串数组，并给定一个字符串对 similarPairs，
 * 其中 similarPairs[i] = [xi, yi] 表示两个单词 xi 和 yi 是相似的。
 * 如果 sentence1 和 sentence2 相似则返回 true，如果不相似则返回 false。
 * 
 * 两个句子是相似的，如果：
 * 1. 它们具有相同的长度（即相同的字数）
 * 2. sentence1[i] 和 sentence2[i] 是相似的
 * 
 * 注意，一个词总是与它自己相似。也注意相似关系是可传递的。
 * 例如，如果单词 a 和 b 是相似的，单词 b 和 c 也是相似的，那么 a 和 c 也是相似的。
 * 
 * 示例 1:
 * 输入: 
 * sentence1 = ["great","acting","skills"]
 * sentence2 = ["fine","drama","talent"]
 * similarPairs = [["great","good"],["fine","good"],["drama","acting"],["skills","talent"]]
 * 输出: true
 * 解释: 这两个句子长度相同，每个单词都相似。
 * 
 * 示例 2:
 * 输入: 
 * sentence1 = ["I","love","leetcode"]
 * sentence2 = ["I","love","onepiece"]
 * similarPairs = [["manga","onepiece"],["platform","anime"],["leetcode","platform"],["anime","manga"]]
 * 输出: true
 * 解释: "leetcode" --> "platform" --> "anime" --> "manga" --> "onepiece"
 * 因为“leetcode”和“onepiece”相似，而且前两个单词相同，所以这两个句子是相似的。
 * 
 * 示例 3:
 * 输入: 
 * sentence1 = ["I","love","leetcode"]
 * sentence2 = ["I","love","onepiece"]
 * similarPairs = [["manga","hunterXhunter"],["platform","anime"],["leetcode","platform"],["anime","manga"]]
 * 输出: false
 * 解释: “leetcode”和“onepiece”不相似。
 * 
 * 约束条件：
 * 1 <= sentence1.length, sentence2.length <= 1000
 * 1 <= sentence1[i].length, sentence2[i].length <= 20
 * sentence1[i] 和 sentence2[i] 只包含英文字母
 * 0 <= similarPairs.length <= 2000
 * similarPairs[i].length == 2
 * 1 <= xi.length, yi.length <= 20
 * xi 和 yi 只包含英文字母
 * 
 * 测试链接: https://leetcode.cn/problems/sentence-similarity-ii/
 * 相关平台: LeetCode 737
 */
public class Code09_SentenceSimilarityII {
    
    /**
     * 使用并查集解决句子相似性问题
     * 
     * 解题思路：
     * 1. 首先检查两个句子长度是否相等，不相等直接返回false
     * 2. 使用并查集来处理相似单词的传递性关系
     * 3. 将所有相似单词对加入并查集
     * 4. 对于句子中的每一对单词，检查它们是否在同一个集合中
     * 
     * 时间复杂度：O(N + P * α(P))，其中N是句子长度，P是相似单词对数量，α是阿克曼函数的反函数
     * 空间复杂度：O(P)
     * 
     * @param sentence1 第一个句子
     * @param sentence2 第二个句子
     * @param similarPairs 相似单词对
     * @return 如果两个句子相似返回true，否则返回false
     */
    public static boolean areSentencesSimilarTwo(String[] sentence1, String[] sentence2, List<List<String>> similarPairs) {
        // 边界条件检查
        if (sentence1 == null || sentence2 == null) {
            return sentence1 == sentence2;
        }
        
        // 长度不同直接返回false
        if (sentence1.length != sentence2.length) {
            return false;
        }
        
        // 创建并查集
        UnionFind uf = new UnionFind();
        
        // 将所有相似单词对加入并查集
        for (List<String> pair : similarPairs) {
            uf.union(pair.get(0), pair.get(1));
        }
        
        // 检查每一对单词是否相似
        for (int i = 0; i < sentence1.length; i++) {
            // 单词相同或者在同一个集合中则相似
            if (!sentence1[i].equals(sentence2[i]) && 
                !uf.isConnected(sentence1[i], sentence2[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 并查集数据结构实现
     * 包含路径压缩优化
     */
    static class UnionFind {
        private Map<String, String> parent;  // parent[word]表示单词word的父节点
        
        /**
         * 初始化并查集
         */
        public UnionFind() {
            parent = new HashMap<>();
        }
        
        /**
         * 查找单词的根节点（代表元素）
         * 使用路径压缩优化
         * @param x 要查找的单词
         * @return 单词x所在集合的根节点
         */
        public String find(String x) {
            // 如果单词不在并查集中，将其加入并设置为自己的父节点
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                return x;
            }
            
            // 路径压缩：将路径上的所有节点直接连接到根节点
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }
        
        /**
         * 合并两个单词所在的集合
         * @param x 第一个单词
         * @param y 第二个单词
         */
        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            // 如果已经在同一个集合中，则无需合并
            if (!rootX.equals(rootY)) {
                parent.put(rootX, rootY);
            }
        }
        
        /**
         * 判断两个单词是否在同一个集合中
         * @param x 第一个单词
         * @param y 第二个单词
         * @return 如果在同一个集合中返回true，否则返回false
         */
        public boolean isConnected(String x, String y) {
            return find(x).equals(find(y));
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String[] sentence1_1 = {"great", "acting", "skills"};
        String[] sentence2_1 = {"fine", "drama", "talent"};
        List<List<String>> similarPairs1 = new ArrayList<>();
        similarPairs1.add(Arrays.asList("great", "good"));
        similarPairs1.add(Arrays.asList("fine", "good"));
        similarPairs1.add(Arrays.asList("drama", "acting"));
        similarPairs1.add(Arrays.asList("skills", "talent"));
        
        System.out.println("测试用例1结果: " + areSentencesSimilarTwo(sentence1_1, sentence2_1, similarPairs1)); // 预期输出: true
        
        // 测试用例2
        String[] sentence1_2 = {"I", "love", "leetcode"};
        String[] sentence2_2 = {"I", "love", "onepiece"};
        List<List<String>> similarPairs2 = new ArrayList<>();
        similarPairs2.add(Arrays.asList("manga", "onepiece"));
        similarPairs2.add(Arrays.asList("platform", "anime"));
        similarPairs2.add(Arrays.asList("leetcode", "platform"));
        similarPairs2.add(Arrays.asList("anime", "manga"));
        
        System.out.println("测试用例2结果: " + areSentencesSimilarTwo(sentence1_2, sentence2_2, similarPairs2)); // 预期输出: true
        
        // 测试用例3
        String[] sentence1_3 = {"I", "love", "leetcode"};
        String[] sentence2_3 = {"I", "love", "onepiece"};
        List<List<String>> similarPairs3 = new ArrayList<>();
        similarPairs3.add(Arrays.asList("manga", "hunterXhunter"));
        similarPairs3.add(Arrays.asList("platform", "anime"));
        similarPairs3.add(Arrays.asList("leetcode", "platform"));
        similarPairs3.add(Arrays.asList("anime", "manga"));
        
        System.out.println("测试用例3结果: " + areSentencesSimilarTwo(sentence1_3, sentence2_3, similarPairs3)); // 预期输出: false
        
        // 测试用例4：相同句子
        String[] sentence1_4 = {"hello", "world"};
        String[] sentence2_4 = {"hello", "world"};
        List<List<String>> similarPairs4 = new ArrayList<>();
        
        System.out.println("测试用例4结果: " + areSentencesSimilarTwo(sentence1_4, sentence2_4, similarPairs4)); // 预期输出: true
    }
}