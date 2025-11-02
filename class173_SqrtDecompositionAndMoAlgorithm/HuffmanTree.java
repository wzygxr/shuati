package class175.霍夫曼树;

import java.util.*;

/**
 * 霍夫曼树（Huffman Tree）实现类
 * 霍夫曼树是一种带权路径长度最短的二叉树，也称为最优二叉树
 * 
 * 常见应用场景：
 * 1. 数据压缩（霍夫曼编码）
 * 2. 文件压缩算法（如ZIP、GZIP等）
 * 3. 信息论中的最优编码
 * 4. 通讯中的数据传输优化
 * 5. 频率相关的排序和检索
 * 
 * 相关算法题目：
 * - LeetCode 1161. 最大层内元素和 https://leetcode.cn/problems/maximum-level-sum-of-a-binary-tree/
 * - LeetCode 1676. 二叉树的最近公共祖先 IV https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iv/
 * - LeetCode 199. 二叉树的右视图 https://leetcode.cn/problems/binary-tree-right-side-view/
 * - LintCode 913. 二叉树的序列化与反序列化 https://www.lintcode.com/problem/913/
 * - 洛谷 P1090 合并果子 https://www.luogu.com.cn/problem/P1090
 * - 牛客 NC139 数据流中的中位数 https://www.nowcoder.com/practice/9be0172896bd43948f8a32fb954e1be1
 * - HackerRank Huffman Coding https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
 * - CodeChef HUFFMAN https://www.codechef.com/problems/HUFFMAN
 * - USACO Huffman Coding https://usaco.org/index.php?page=viewproblem2&cpid=689
 * - AtCoder ABC240 E - Ranges on Tree https://atcoder.jp/contests/abc240/tasks/abc240_e
 * - 杭电 OJ 1053 Entropy https://acm.hdu.edu.cn/showproblem.php?pid=1053
 */

class HuffmanNode {
    public char data;       // 字符数据
    public int frequency;   // 出现频率
    public HuffmanNode left; // 左子节点
    public HuffmanNode right; // 右子节点

    /**
     * 构造函数
     * @param data 字符数据
     * @param frequency 出现频率
     */
    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    /**
     * 构造函数（用于内部节点）
     * @param frequency 出现频率
     */
    public HuffmanNode(int frequency) {
        this.data = ' ';
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
}

public class HuffmanTree {
    private HuffmanNode root; // 霍夫曼树的根节点

    /**
     * 构造函数，通过字符频率表构建霍夫曼树
     * @param frequencyMap 字符频率映射表
     */
    public HuffmanTree(Map<Character, Integer> frequencyMap) {
        buildTree(frequencyMap);
    }

    /**
     * 构建霍夫曼树
     * @param frequencyMap 字符频率映射表
     */
    private void buildTree(Map<Character, Integer> frequencyMap) {
        // 创建优先队列（最小堆），按照频率排序
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        // 将所有字符节点加入优先队列
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // 构建霍夫曼树
        while (pq.size() > 1) {
            // 取出两个最小频率的节点
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            // 创建新的内部节点，频率为两个子节点的频率之和
            HuffmanNode internalNode = new HuffmanNode(left.frequency + right.frequency);
            internalNode.left = left;
            internalNode.right = right;

            // 将内部节点加入队列
            pq.offer(internalNode);
        }

        // 队中只剩下根节点
        root = pq.poll();
    }

    /**
     * 获取霍夫曼编码表
     * @return 字符到霍夫曼编码的映射
     */
    public Map<Character, String> getHuffmanCodes() {
        Map<Character, String> codes = new HashMap<>();
        if (root != null) {
            generateCodes(root, "", codes);
        }
        return codes;
    }

    /**
     * 递归生成霍夫曼编码
     * @param node 当前节点
     * @param currentCode 当前编码
     * @param codes 编码映射表
     */
    private void generateCodes(HuffmanNode node, String currentCode, Map<Character, String> codes) {
        if (node == null) {
            return;
        }

        // 如果是叶子节点，保存编码
        if (node.left == null && node.right == null) {
            codes.put(node.data, currentCode.isEmpty() ? "0" : currentCode);
            return;
        }

        // 递归遍历左右子树
        generateCodes(node.left, currentCode + "0", codes);
        generateCodes(node.right, currentCode + "1", codes);
    }

    /**
     * 编码文本
     * @param text 原始文本
     * @return 霍夫曼编码后的二进制字符串
     */
    public String encode(String text) {
        Map<Character, String> codes = getHuffmanCodes();
        StringBuilder encoded = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (codes.containsKey(c)) {
                encoded.append(codes.get(c));
            } else {
                throw new IllegalArgumentException("字符 " + c + " 不在霍夫曼树中");
            }
        }

        return encoded.toString();
    }

    /**
     * 解码二进制字符串
     * @param encoded 霍夫曼编码的二进制字符串
     * @return 解码后的文本
     */
    public String decode(String encoded) {
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;

        for (char bit : encoded.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else if (bit == '1') {
                current = current.right;
            } else {
                throw new IllegalArgumentException("无效的二进制位: " + bit);
            }

            // 到达叶子节点
            if (current.left == null && current.right == null) {
                decoded.append(current.data);
                current = root; // 重置到根节点
            }
        }

        return decoded.toString();
    }

    /**
     * 计算霍夫曼树的带权路径长度（WPL）
     * @return 带权路径长度
     */
    public int calculateWPL() {
        return calculateWPL(root, 0);
    }

    /**
     * 递归计算带权路径长度
     * @param node 当前节点
     * @param depth 当前深度
     * @return 带权路径长度
     */
    private int calculateWPL(HuffmanNode node, int depth) {
        if (node == null) {
            return 0;
        }

        // 叶子节点
        if (node.left == null && node.right == null) {
            return node.frequency * depth;
        }

        // 内部节点，递归计算左右子树
        return calculateWPL(node.left, depth + 1) + calculateWPL(node.right, depth + 1);
    }

    /**
     * 打印霍夫曼树的结构
     */
    public void printTree() {
        System.out.println("霍夫曼树结构：");
        printTree(root, 0);
    }

    /**
     * 递归打印树结构
     * @param node 当前节点
     * @param level 当前层级
     */
    private void printTree(HuffmanNode node, int level) {
        if (node == null) {
            return;
        }

        // 打印右子树
        printTree(node.right, level + 1);

        // 打印当前节点
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        if (node.data == ' ') {
            System.out.println("[内部] " + node.frequency);
        } else {
            System.out.println("['" + node.data + "'] " + node.frequency);
        }

        // 打印左子树
        printTree(node.left, level + 1);
    }

    /**
     * 获取树的高度
     * @return 树的高度
     */
    public int getHeight() {
        return getHeight(root);
    }

    /**
     * 递归计算树的高度
     * @param node 当前节点
     * @return 树的高度
     */
    private int getHeight(HuffmanNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * 统计叶子节点数量
     * @return 叶子节点数量
     */
    public int getLeafCount() {
        return getLeafCount(root);
    }

    /**
     * 递归统计叶子节点数量
     * @param node 当前节点
     * @return 叶子节点数量
     */
    private int getLeafCount(HuffmanNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return getLeafCount(node.left) + getLeafCount(node.right);
    }

    /**
     * 根据文本自动构建频率表并创建霍夫曼树
     * @param text 输入文本
     * @return 构建的霍夫曼树
     */
    public static HuffmanTree buildFromText(String text) {
        // 统计字符频率
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return new HuffmanTree(frequencyMap);
    }

    /**
     * 获取根节点
     * @return 根节点
     */
    public HuffmanNode getRoot() {
        return root;
    }

    /**
     * 检查两棵霍夫曼树是否相同
     * @param other 另一棵霍夫曼树
     * @return 如果相同返回true，否则返回false
     */
    public boolean equals(HuffmanTree other) {
        if (other == null) {
            return false;
        }
        return equals(root, other.getRoot());
    }

    /**
     * 递归比较两棵子树是否相同
     * @param node1 第一棵树的节点
     * @param node2 第二棵树的节点
     * @return 如果相同返回true，否则返回false
     */
    private boolean equals(HuffmanNode node1, HuffmanNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        }
        if (node1 == null || node2 == null) {
            return false;
        }
        // 比较当前节点的频率
        if (node1.frequency != node2.frequency) {
            return false;
        }
        // 如果是叶子节点，还需要比较字符
        if (node1.left == null && node1.right == null && node2.left == null && node2.right == null) {
            return node1.data == node2.data;
        }
        // 递归比较左右子树
        return equals(node1.left, node2.left) && equals(node1.right, node2.right);
    }

    /**
     * 层序遍历霍夫曼树
     * @return 层序遍历结果
     */
    public List<List<String>> levelOrderTraversal() {
        List<List<String>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<HuffmanNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<String> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                HuffmanNode node = queue.poll();
                if (node.data == ' ') {
                    currentLevel.add("[内部] " + node.frequency);
                } else {
                    currentLevel.add("['" + node.data + "'] " + node.frequency);
                }

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            result.add(currentLevel);
        }

        return result;
    }

    /**
     * 计算压缩率
     * @param originalText 原始文本
     * @return 压缩率（压缩后大小/原始大小）
     */
    public double getCompressionRatio(String originalText) {
        // 假设原始文本每个字符占用8位
        int originalSize = originalText.length() * 8;
        
        // 计算压缩后的大小（以位为单位）
        String encoded = encode(originalText);
        int compressedSize = encoded.length();
        
        return (double) compressedSize / originalSize;
    }

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试数据：字符及其频率
        Map<Character, Integer> frequencyMap = new HashMap<>();
        frequencyMap.put('a', 5);
        frequencyMap.put('b', 9);
        frequencyMap.put('c', 12);
        frequencyMap.put('d', 13);
        frequencyMap.put('e', 16);
        frequencyMap.put('f', 45);

        // 创建霍夫曼树
        HuffmanTree huffmanTree = new HuffmanTree(frequencyMap);

        // 打印树结构
        huffmanTree.printTree();

        // 获取霍夫曼编码
        Map<Character, String> codes = huffmanTree.getHuffmanCodes();
        System.out.println("\n霍夫曼编码：");
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 计算WPL
        System.out.println("\n带权路径长度(WPL): " + huffmanTree.calculateWPL());

        // 计算树高和叶子节点数
        System.out.println("树高: " + huffmanTree.getHeight());
        System.out.println("叶子节点数: " + huffmanTree.getLeafCount());

        // 层序遍历
        System.out.println("\n层序遍历：");
        List<List<String>> levelOrder = huffmanTree.levelOrderTraversal();
        for (int i = 0; i < levelOrder.size(); i++) {
            System.out.println("层 " + (i + 1) + ": " + levelOrder.get(i));
        }

        // 测试编码和解码
        String text = "abcdef"; // 测试文本
        try {
            String encoded = huffmanTree.encode(text);
            String decoded = huffmanTree.decode(encoded);
            
            System.out.println("\n原始文本: " + text);
            System.out.println("编码后: " + encoded);
            System.out.println("解码后: " + decoded);
            System.out.println("编码解码一致性: " + text.equals(decoded));
            System.out.println("压缩率: " + huffmanTree.getCompressionRatio(text));
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }

        // 测试从文本构建霍夫曼树
        System.out.println("\n从文本构建霍夫曼树：");
        String testText = "hello huffman coding!";  
        HuffmanTree treeFromText = HuffmanTree.buildFromText(testText);
        Map<Character, String> codesFromText = treeFromText.getHuffmanCodes();
        System.out.println("文本霍夫曼编码：");
        for (Map.Entry<Character, String> entry : codesFromText.entrySet()) {
            System.out.println("'" + entry.getKey() + "'"); // 打印键
            System.out.println(" : " + entry.getValue()); // 打印值
        }
        
        // 测试编码解码
        String encodedText = treeFromText.encode(testText);
        String decodedText = treeFromText.decode(encodedText);
        System.out.println("\n原始文本长度: " + testText.length() + " 字符");
        System.out.println("编码后长度: " + encodedText.length() + " 位");
        System.out.println("解码后: " + decodedText);
        System.out.println("解码正确性: " + testText.equals(decodedText));
        System.out.println("压缩率: " + treeFromText.getCompressionRatio(testText));
    }
}