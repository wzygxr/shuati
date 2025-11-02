package class175.决策树;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 决策树（Decision Tree）实现
 * 决策树是一种用于分类和回归的监督学习算法
 * 它通过从数据特征中学习决策规则，构建树状模型进行预测
 * 
 * 常见应用场景：
 * 1. 分类问题（如疾病诊断、客户流失预测）
 * 2. 回归问题（如房价预测、销售量预测）
 * 3. 特征重要性分析
 * 4. 规则提取（从树结构中提取业务规则）
 * 5. 数据分析和数据挖掘
 * 6. 风险评估和信用评分
 * 
 * 相关算法题目：
 * - LeetCode 222. 完全二叉树的节点个数 https://leetcode.cn/problems/count-complete-tree-nodes/
 * - LeetCode 637. 二叉树的层平均值 https://leetcode.cn/problems/average-of-levels-in-binary-tree/
 * - LeetCode 572. 另一棵树的子树 https://leetcode.cn/problems/subtree-of-another-tree/
 * - LintCode 95. 验证二叉查找树 https://www.lintcode.com/problem/95/
 * - 洛谷 P3379 【模板】最近公共祖先 https://www.luogu.com.cn/problem/P3379
 * - 牛客 NC119 最小的K个数 https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
 * - HackerRank Tree: Huffman Decoding https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
 * - CodeChef DECMTREE https://www.codechef.com/problems/DECMTREE
 * - USACO Section 3.4 Raucous Rockers https://usaco.org/index.php?page=viewproblem2&cpid=348
 * - AtCoder ABC177 F - I hate Shortest Path Problem https://atcoder.jp/contests/abc177/tasks/abc177_f
 * - 杭电 OJ 2544 最短路 https://acm.hdu.edu.cn/showproblem.php?pid=2544
 * - SPOJ PT07Z - Longest path in a tree https://www.spoj.com/problems/PT07Z/
 * - Codeforces 1327C - Game with Chips https://codeforces.com/problemset/problem/1327/C
 */

/**
 * 决策树节点类
 */
class TreeNode {
    private boolean isLeaf;         // 是否为叶节点
    private String attribute;       // 用于分裂的属性
    private Map<String, TreeNode> children;  // 子节点映射
    private String classification;  // 叶节点的分类结果
    private double value;           // 回归树的预测值（用于回归问题）
    private double informationGain; // 分裂时的信息增益
    private int depth;              // 节点深度
    
    /**
     * 构造内部节点（用于属性分裂）
     * @param attribute 分裂属性
     * @param informationGain 信息增益
     * @param depth 节点深度
     */
    public TreeNode(String attribute, double informationGain, int depth) {
        this.isLeaf = false;
        this.attribute = attribute;
        this.informationGain = informationGain;
        this.depth = depth;
        this.children = new HashMap<>();
    }
    
    /**
     * 构造叶节点（分类树）
     * @param classification 分类结果
     * @param depth 节点深度
     */
    public TreeNode(String classification, int depth) {
        this.isLeaf = true;
        this.classification = classification;
        this.depth = depth;
        this.children = new HashMap<>();
    }
    
    /**
     * 构造叶节点（回归树）
     * @param value 回归预测值
     * @param depth 节点深度
     */
    public TreeNode(double value, int depth) {
        this.isLeaf = true;
        this.value = value;
        this.depth = depth;
        this.children = new HashMap<>();
    }
    
    // Getter和Setter方法
    public boolean isLeaf() { return isLeaf; }
    public String getAttribute() { return attribute; }
    public Map<String, TreeNode> getChildren() { return children; }
    public String getClassification() { return classification; }
    public double getValue() { return value; }
    public double getInformationGain() { return informationGain; }
    public int getDepth() { return depth; }
    
    /**
     * 添加子节点
     * @param attributeValue 属性值
     * @param child 子节点
     */
    public void addChild(String attributeValue, TreeNode child) {
        children.put(attributeValue, child);
    }
    
    @Override
    public String toString() {
        if (isLeaf) {
            return classification != null ? 
                   "叶节点(分类=" + classification + ", 深度=" + depth + ")" : 
                   "叶节点(值=" + value + ", 深度=" + depth + ")";
        } else {
            return "内部节点(属性=" + attribute + ", 信息增益=" + 
                   String.format("%.4f", informationGain) + ", 深度=" + depth + ")";
        }
    }
}

/**
 * 数据集行类，用于表示数据集中的一行数据
 */
class DataRow {
    private Map<String, Object> attributes;  // 属性及其值
    private String label;                    // 分类标签
    private Double value;                    // 回归目标值
    
    /**
     * 构造分类数据行
     * @param attributes 属性映射
     * @param label 分类标签
     */
    public DataRow(Map<String, Object> attributes, String label) {
        this.attributes = new HashMap<>(attributes);
        this.label = label;
        this.value = null;
    }
    
    /**
     * 构造回归数据行
     * @param attributes 属性映射
     * @param value 回归目标值
     */
    public DataRow(Map<String, Object> attributes, double value) {
        this.attributes = new HashMap<>(attributes);
        this.label = null;
        this.value = value;
    }
    
    /**
     * 获取属性值
     * @param attribute 属性名
     * @return 属性值
     */
    public Object getAttributeValue(String attribute) {
        return attributes.get(attribute);
    }
    
    /**
     * 获取所有属性
     * @return 属性映射
     */
    public Map<String, Object> getAllAttributes() {
        return new HashMap<>(attributes);
    }
    
    /**
     * 获取分类标签
     * @return 分类标签
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * 获取回归目标值
     * @return 目标值
     */
    public Double getValue() {
        return value;
    }
    
    /**
     * 判断是否为分类数据
     * @return 是否为分类数据
     */
    public boolean isClassification() {
        return label != null;
    }
    
    @Override
    public String toString() {
        if (isClassification()) {
            return "DataRow{attributes=" + attributes + ", label='" + label + "'}";
        } else {
            return "DataRow{attributes=" + attributes + ", value=" + value + "}";
        }
    }
}

/**
 * 决策树类
 */
public class DecisionTree {
    private TreeNode root;           // 根节点
    private List<String> attributes; // 特征列表
    private int maxDepth;            // 最大深度
    private int minSamplesSplit;     // 最小分裂样本数
    private boolean isRegression;    // 是否为回归树
    
    /**
     * 构造函数
     * @param maxDepth 最大深度
     * @param minSamplesSplit 最小分裂样本数
     * @param isRegression 是否为回归树
     */
    public DecisionTree(int maxDepth, int minSamplesSplit, boolean isRegression) {
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
        this.isRegression = isRegression;
        this.root = null;
    }
    
    /**
     * 默认构造函数（分类树）
     */
    public DecisionTree() {
        this.maxDepth = 10;           // 默认最大深度
        this.minSamplesSplit = 2;     // 默认最小分裂样本数
        this.isRegression = false;    // 默认分类树
        this.root = null;
    }
    
    /**
     * 训练决策树
     * @param dataSet 数据集
     * @param attributes 属性列表
     */
    public void fit(List<DataRow> dataSet, List<String> attributes) {
        this.attributes = new ArrayList<>(attributes);
        this.root = buildTree(dataSet, attributes, 0);
    }
    
    /**
     * 递归构建决策树
     * @param dataSet 当前数据集
     * @param availableAttributes 可用属性列表
     * @param depth 当前深度
     * @return 构建的节点
     */
    private TreeNode buildTree(List<DataRow> dataSet, List<String> availableAttributes, int depth) {
        // 检查是否满足终止条件
        if (shouldStop(dataSet, availableAttributes, depth)) {
            return createLeafNode(dataSet, depth);
        }
        
        // 选择最佳分裂属性
        String bestAttribute = selectBestAttribute(dataSet, availableAttributes);
        double bestGain = calculateGain(dataSet, bestAttribute);
        
        // 创建内部节点
        TreeNode node = new TreeNode(bestAttribute, bestGain, depth);
        
        // 根据最佳属性的值分割数据集
        Map<Object, List<DataRow>> partitions = partitionByAttribute(dataSet, bestAttribute);
        
        // 为每个属性值创建子树
        for (Map.Entry<Object, List<DataRow>> entry : partitions.entrySet()) {
            Object value = entry.getKey();
            List<DataRow> subset = entry.getValue();
            
            // 如果子集为空，创建叶节点
            if (subset.isEmpty()) {
                node.addChild(value.toString(), createLeafNode(dataSet, depth + 1));
            } else {
                // 递归构建子树
                List<String> newAttributes = new ArrayList<>(availableAttributes);
                newAttributes.remove(bestAttribute);
                node.addChild(value.toString(), buildTree(subset, newAttributes, depth + 1));
            }
        }
        
        return node;
    }
    
    /**
     * 判断是否应该停止树的生长
     * @param dataSet 当前数据集
     * @param availableAttributes 可用属性列表
     * @param depth 当前深度
     * @return 是否应该停止
     */
    private boolean shouldStop(List<DataRow> dataSet, List<String> availableAttributes, int depth) {
        // 1. 数据集为空
        if (dataSet.isEmpty()) {
            return true;
        }
        
        // 2. 达到最大深度
        if (depth >= maxDepth) {
            return true;
        }
        
        // 3. 没有可用属性
        if (availableAttributes.isEmpty()) {
            return true;
        }
        
        // 4. 样本数小于最小分裂样本数
        if (dataSet.size() < minSamplesSplit) {
            return true;
        }
        
        // 5. 所有样本属于同一类别（分类树）
        if (!isRegression) {
            String firstLabel = dataSet.get(0).getLabel();
            boolean allSame = dataSet.stream().allMatch(row -> row.getLabel().equals(firstLabel));
            if (allSame) {
                return true;
            }
        } 
        // 5. 所有样本的目标值相同（回归树）
        else {
            double firstValue = dataSet.get(0).getValue();
            boolean allSame = dataSet.stream().allMatch(row -> Math.abs(row.getValue() - firstValue) < 1e-9);
            if (allSame) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 创建叶节点
     * @param dataSet 数据集
     * @param depth 深度
     * @return 叶节点
     */
    private TreeNode createLeafNode(List<DataRow> dataSet, int depth) {
        if (dataSet.isEmpty()) {
            return isRegression ? new TreeNode(0.0, depth) : new TreeNode("Unknown", depth);
        }
        
        if (!isRegression) {
            // 分类树：返回最常见的类别
            Map<String, Long> labelCounts = dataSet.stream()
                    .collect(Collectors.groupingBy(DataRow::getLabel, Collectors.counting()));
            
            String mostCommonLabel = labelCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Unknown");
            
            return new TreeNode(mostCommonLabel, depth);
        } else {
            // 回归树：返回平均值
            double average = dataSet.stream()
                    .mapToDouble(DataRow::getValue)
                    .average()
                    .orElse(0.0);
            
            return new TreeNode(average, depth);
        }
    }
    
    /**
     * 选择最佳分裂属性
     * @param dataSet 数据集
     * @param availableAttributes 可用属性列表
     * @return 最佳属性名
     */
    private String selectBestAttribute(List<DataRow> dataSet, List<String> availableAttributes) {
        String bestAttribute = null;
        double bestGain = -Double.MAX_VALUE;
        
        for (String attribute : availableAttributes) {
            double gain = calculateGain(dataSet, attribute);
            if (gain > bestGain) {
                bestGain = gain;
                bestAttribute = attribute;
            }
        }
        
        return bestAttribute;
    }
    
    /**
     * 计算信息增益（分类树）或方差减少（回归树）
     * @param dataSet 数据集
     * @param attribute 属性名
     * @return 信息增益或方差减少量
     */
    private double calculateGain(List<DataRow> dataSet, String attribute) {
        if (dataSet.isEmpty()) {
            return 0.0;
        }
        
        if (!isRegression) {
            // 计算信息增益（使用熵）
            double parentEntropy = calculateEntropy(dataSet);
            Map<Object, List<DataRow>> partitions = partitionByAttribute(dataSet, attribute);
            
            double weightedEntropy = 0.0;
            for (List<DataRow> subset : partitions.values()) {
                double weight = (double) subset.size() / dataSet.size();
                weightedEntropy += weight * calculateEntropy(subset);
            }
            
            return parentEntropy - weightedEntropy;
        } else {
            // 计算方差减少
            double parentVariance = calculateVariance(dataSet);
            Map<Object, List<DataRow>> partitions = partitionByAttribute(dataSet, attribute);
            
            double weightedVariance = 0.0;
            for (List<DataRow> subset : partitions.values()) {
                double weight = (double) subset.size() / dataSet.size();
                weightedVariance += weight * calculateVariance(subset);
            }
            
            return parentVariance - weightedVariance;
        }
    }
    
    /**
     * 计算数据集的熵
     * @param dataSet 数据集
     * @return 熵值
     */
    private double calculateEntropy(List<DataRow> dataSet) {
        if (dataSet.isEmpty()) {
            return 0.0;
        }
        
        Map<String, Long> labelCounts = dataSet.stream()
                .collect(Collectors.groupingBy(DataRow::getLabel, Collectors.counting()));
        
        double entropy = 0.0;
        double total = dataSet.size();
        
        for (long count : labelCounts.values()) {
            double probability = count / total;
            entropy -= probability * Math.log(probability) / Math.log(2);
        }
        
        return entropy;
    }
    
    /**
     * 计算数据集的方差
     * @param dataSet 数据集
     * @return 方差
     */
    private double calculateVariance(List<DataRow> dataSet) {
        if (dataSet.isEmpty()) {
            return 0.0;
        }
        
        double mean = dataSet.stream().mapToDouble(DataRow::getValue).average().orElse(0.0);
        
        return dataSet.stream()
                .mapToDouble(row -> Math.pow(row.getValue() - mean, 2))
                .average()
                .orElse(0.0);
    }
    
    /**
     * 根据属性值分割数据集
     * @param dataSet 数据集
     * @param attribute 属性名
     * @return 分割后的数据集映射
     */
    private Map<Object, List<DataRow>> partitionByAttribute(List<DataRow> dataSet, String attribute) {
        return dataSet.stream()
                .collect(Collectors.groupingBy(row -> row.getAttributeValue(attribute)));
    }
    
    /**
     * 预测单个样本
     * @param attributes 属性映射
     * @return 预测结果
     */
    public Object predict(Map<String, Object> attributes) {
        if (root == null) {
            throw new IllegalStateException("决策树尚未训练");
        }
        
        return predictRecursive(root, attributes);
    }
    
    /**
     * 递归预测
     * @param node 当前节点
     * @param attributes 属性映射
     * @return 预测结果
     */
    private Object predictRecursive(TreeNode node, Map<String, Object> attributes) {
        // 如果是叶节点，返回分类或回归值
        if (node.isLeaf()) {
            return node.getClassification() != null ? node.getClassification() : node.getValue();
        }
        
        // 获取当前节点用于分裂的属性
        String splitAttribute = node.getAttribute();
        Object attributeValue = attributes.get(splitAttribute);
        
        // 如果找不到对应属性值的子节点，返回最常见的类别或平均值（简化处理）
        if (attributeValue == null || !node.getChildren().containsKey(attributeValue.toString())) {
            return getDefaultPrediction(node);
        }
        
        // 递归到子节点
        TreeNode child = node.getChildren().get(attributeValue.toString());
        return predictRecursive(child, attributes);
    }
    
    /**
     * 获取默认预测值（当无法找到路径时）
     * @param node 当前节点
     * @return 默认预测值
     */
    private Object getDefaultPrediction(TreeNode node) {
        // 这里简化处理，实际应该根据子节点情况返回最常见的分类或平均值
        // 为了简化，对于分类树返回Unknown，对于回归树返回0
        return isRegression ? 0.0 : "Unknown";
    }
    
    /**
     * 批量预测
     * @param dataSet 测试数据集
     * @return 预测结果列表
     */
    public List<Object> predictAll(List<DataRow> dataSet) {
        return dataSet.stream()
                .map(row -> predict(row.getAllAttributes()))
                .collect(Collectors.toList());
    }
    
    /**
     * 计算分类准确率
     * @param testData 测试数据
     * @return 准确率
     */
    public double calculateAccuracy(List<DataRow> testData) {
        if (isRegression) {
            throw new IllegalStateException("不能对回归树计算准确率，请使用均方误差等指标");
        }
        
        int correctCount = 0;
        for (DataRow row : testData) {
            String prediction = (String) predict(row.getAllAttributes());
            if (prediction.equals(row.getLabel())) {
                correctCount++;
            }
        }
        
        return (double) correctCount / testData.size();
    }
    
    /**
     * 计算均方误差（MSE）
     * @param testData 测试数据
     * @return 均方误差
     */
    public double calculateMSE(List<DataRow> testData) {
        if (!isRegression) {
            throw new IllegalStateException("不能对分类树计算均方误差，请使用准确率等指标");
        }
        
        double sumSquaredErrors = 0.0;
        for (DataRow row : testData) {
            double prediction = (double) predict(row.getAllAttributes());
            double actual = row.getValue();
            sumSquaredErrors += Math.pow(prediction - actual, 2);
        }
        
        return sumSquaredErrors / testData.size();
    }
    
    /**
     * 打印决策树
     */
    public void printTree() {
        if (root == null) {
            System.out.println("决策树为空");
            return;
        }
        
        System.out.println("决策树结构：");
        printTreeRecursive(root, "");
    }
    
    /**
     * 递归打印树结构
     * @param node 当前节点
     * @param prefix 前缀
     */
    private void printTreeRecursive(TreeNode node, String prefix) {
        System.out.println(prefix + node);
        
        if (!node.isLeaf()) {
            for (Map.Entry<String, TreeNode> entry : node.getChildren().entrySet()) {
                String value = entry.getKey();
                TreeNode child = entry.getValue();
                System.out.println(prefix + "  |__ " + node.getAttribute() + " = " + value);
                printTreeRecursive(child, prefix + "      ");
            }
        }
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    public int getHeight() {
        return calculateHeight(root);
    }
    
    /**
     * 递归计算树高
     * @param node 当前节点
     * @return 树高
     */
    private int calculateHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        if (node.isLeaf()) {
            return 1;
        }
        
        int maxChildHeight = 0;
        for (TreeNode child : node.getChildren().values()) {
            int childHeight = calculateHeight(child);
            maxChildHeight = Math.max(maxChildHeight, childHeight);
        }
        
        return maxChildHeight + 1;
    }
    
    /**
     * 获取节点数量
     * @return 节点数量
     */
    public int getNodeCount() {
        return countNodes(root);
    }
    
    /**
     * 递归计算节点数量
     * @param node 当前节点
     * @return 节点数量
     */
    private int countNodes(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        int count = 1;  // 当前节点
        for (TreeNode child : node.getChildren().values()) {
            count += countNodes(child);
        }
        
        return count;
    }
    
    /**
     * 获取叶节点数量
     * @return 叶节点数量
     */
    public int getLeafCount() {
        return countLeaves(root);
    }
    
    /**
     * 递归计算叶节点数量
     * @param node 当前节点
     * @return 叶节点数量
     */
    private int countLeaves(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        if (node.isLeaf()) {
            return 1;
        }
        
        int leafCount = 0;
        for (TreeNode child : node.getChildren().values()) {
            leafCount += countLeaves(child);
        }
        
        return leafCount;
    }
    
    /**
     * 获取根节点
     * @return 根节点
     */
    public TreeNode getRoot() {
        return root;
    }
    
    /**
     * 层序遍历决策树
     * @return 层序遍历结果
     */
    public List<List<String>> levelOrderTraversal() {
        List<List<String>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<String> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.toString());
                
                if (!node.isLeaf()) {
                    for (TreeNode child : node.getChildren().values()) {
                        queue.offer(child);
                    }
                }
            }
            
            result.add(currentLevel);
        }
        
        return result;
    }
    
    /**
     * 主函数，用于测试决策树
     */
    public static void main(String[] args) {
        // 创建示例数据（天气数据集 - 分类问题）
        List<DataRow> weatherData = new ArrayList<>();
        List<String> attributes = Arrays.asList("Outlook", "Temperature", "Humidity", "Windy");
        
        // 添加训练数据
        Map<String, Object> attr1 = new HashMap<>();
        attr1.put("Outlook", "Sunny");
        attr1.put("Temperature", "Hot");
        attr1.put("Humidity", "High");
        attr1.put("Windy", false);
        weatherData.add(new DataRow(attr1, "No"));
        
        Map<String, Object> attr2 = new HashMap<>();
        attr2.put("Outlook", "Sunny");
        attr2.put("Temperature", "Hot");
        attr2.put("Humidity", "High");
        attr2.put("Windy", true);
        weatherData.add(new DataRow(attr2, "No"));
        
        Map<String, Object> attr3 = new HashMap<>();
        attr3.put("Outlook", "Overcast");
        attr3.put("Temperature", "Hot");
        attr3.put("Humidity", "High");
        attr3.put("Windy", false);
        weatherData.add(new DataRow(attr3, "Yes"));
        
        Map<String, Object> attr4 = new HashMap<>();
        attr4.put("Outlook", "Rainy");
        attr4.put("Temperature", "Mild");
        attr4.put("Humidity", "High");
        attr4.put("Windy", false);
        weatherData.add(new DataRow(attr4, "Yes"));
        
        Map<String, Object> attr5 = new HashMap<>();
        attr5.put("Outlook", "Rainy");
        attr5.put("Temperature", "Cool");
        attr5.put("Humidity", "Normal");
        attr5.put("Windy", false);
        weatherData.add(new DataRow(attr5, "Yes"));
        
        Map<String, Object> attr6 = new HashMap<>();
        attr6.put("Outlook", "Rainy");
        attr6.put("Temperature", "Cool");
        attr6.put("Humidity", "Normal");
        attr6.put("Windy", true);
        weatherData.add(new DataRow(attr6, "No"));
        
        Map<String, Object> attr7 = new HashMap<>();
        attr7.put("Outlook", "Overcast");
        attr7.put("Temperature", "Cool");
        attr7.put("Humidity", "Normal");
        attr7.put("Windy", true);
        weatherData.add(new DataRow(attr7, "Yes"));
        
        Map<String, Object> attr8 = new HashMap<>();
        attr8.put("Outlook", "Sunny");
        attr8.put("Temperature", "Mild");
        attr8.put("Humidity", "High");
        attr8.put("Windy", false);
        weatherData.add(new DataRow(attr8, "No"));
        
        Map<String, Object> attr9 = new HashMap<>();
        attr9.put("Outlook", "Sunny");
        attr9.put("Temperature", "Cool");
        attr9.put("Humidity", "Normal");
        attr9.put("Windy", false);
        weatherData.add(new DataRow(attr9, "Yes"));
        
        Map<String, Object> attr10 = new HashMap<>();
        attr10.put("Outlook", "Rainy");
        attr10.put("Temperature", "Mild");
        attr10.put("Humidity", "Normal");
        attr10.put("Windy", false);
        weatherData.add(new DataRow(attr10, "Yes"));
        
        // 训练分类决策树
        DecisionTree classifier = new DecisionTree(10, 2, false);
        classifier.fit(weatherData, attributes);
        
        // 打印决策树
        System.out.println("===== 分类决策树 ======");
        classifier.printTree();
        
        // 测试预测
        Map<String, Object> testAttr = new HashMap<>();
        testAttr.put("Outlook", "Sunny");
        testAttr.put("Temperature", "Mild");
        testAttr.put("Humidity", "Normal");
        testAttr.put("Windy", true);
        
        Object prediction = classifier.predict(testAttr);
        System.out.println("\n测试样本预测结果: " + prediction);
        
        // 模型统计信息
        System.out.println("\n树高: " + classifier.getHeight());
        System.out.println("节点数: " + classifier.getNodeCount());
        System.out.println("叶节点数: " + classifier.getLeafCount());
        
        // 计算训练集准确率
        double accuracy = classifier.calculateAccuracy(weatherData);
        System.out.println("训练集准确率: " + String.format("%.2f%%", accuracy * 100));
        
        // 层序遍历
        System.out.println("\n层序遍历：");
        List<List<String>> levelOrder = classifier.levelOrderTraversal();
        for (int i = 0; i < levelOrder.size(); i++) {
            System.out.println("层 " + (i + 1) + ": " + levelOrder.get(i));
        }
        
        // 创建回归树测试数据
        List<DataRow> regressionData = new ArrayList<>();
        List<String> regressionAttrs = Arrays.asList("Size", "Bedrooms", "Bathrooms", "Age");
        
        // 添加房屋价格预测数据（简化示例）
        Map<String, Object> house1 = new HashMap<>();
        house1.put("Size", 1400);
        house1.put("Bedrooms", 3);
        house1.put("Bathrooms", 2);
        house1.put("Age", 10);
        regressionData.add(new DataRow(house1, 200000.0));
        
        Map<String, Object> house2 = new HashMap<>();
        house2.put("Size", 1600);
        house2.put("Bedrooms", 3);
        house2.put("Bathrooms", 2.5);
        house2.put("Age", 5);
        regressionData.add(new DataRow(house2, 250000.0));
        
        Map<String, Object> house3 = new HashMap<>();
        house3.put("Size", 2000);
        house3.put("Bedrooms", 4);
        house3.put("Bathrooms", 3);
        house3.put("Age", 2);
        regressionData.add(new DataRow(house3, 350000.0));
        
        Map<String, Object> house4 = new HashMap<>();
        house4.put("Size", 1200);
        house4.put("Bedrooms", 2);
        house4.put("Bathrooms", 1);
        house4.put("Age", 20);
        regressionData.add(new DataRow(house4, 150000.0));
        
        // 训练回归决策树
        DecisionTree regressor = new DecisionTree(5, 2, true);
        regressor.fit(regressionData, regressionAttrs);
        
        // 打印回归树
        System.out.println("\n===== 回归决策树 ======");
        regressor.printTree();
        
        // 测试预测
        Map<String, Object> testHouse = new HashMap<>();
        testHouse.put("Size", 1800);
        testHouse.put("Bedrooms", 3);
        testHouse.put("Bathrooms", 2);
        testHouse.put("Age", 8);
        
        Object pricePrediction = regressor.predict(testHouse);
        System.out.println("\n测试房屋价格预测: $" + pricePrediction);
        
        // 计算均方误差
        double mse = regressor.calculateMSE(regressionData);
        System.out.println("均方误差: " + String.format("%.2f", mse));
    }
}