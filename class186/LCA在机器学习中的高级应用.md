# LCA在机器学习中的高级应用

## 1. 语法树分析中的LCA应用

### 问题1：句法依存关系分析

**应用场景**：
在自然语言处理中，句法分析树的节点间关系可以通过LCA算法高效计算。

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

const int MAXN = 1e4 + 5;
const int LOG = 15;

class SyntaxTreeAnalyzer {
private:
    vector<int> adj[MAXN];
    vector<string> word;  // 词汇表
    vector<string> pos;   // 词性标注
    int depth[MAXN];
    int up[MAXN][LOG];
    
public:
    SyntaxTreeAnalyzer(int n) {
        word.resize(n + 1);
        pos.resize(n + 1);
    }
    
    void addDependency(int parent, int child, const string& w, const string& p) {
        adj[parent].push_back(child);
        word[child] = w;
        pos[child] = p;
    }
    
    void preprocess(int root) {
        dfs(root, 0);
    }
    
    void dfs(int u, int parent) {
        depth[u] = depth[parent] + 1;
        up[u][0] = parent;
        
        for (int k = 1; k < LOG; k++) {
            up[u][k] = up[up[u][k-1]][k-1];
        }
        
        for (int v : adj[u]) {
            dfs(v, u);
        }
    }
    
    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        
        int diff = depth[u] - depth[v];
        for (int k = 0; k < LOG; k++) {
            if (diff & (1 << k)) {
                u = up[u][k];
            }
        }
        
        if (u == v) return u;
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[u][k] != up[v][k]) {
                u = up[u][k];
                v = up[v][k];
            }
        }
        
        return up[u][0];
    }
    
    // 计算两个词在语法树中的最短路径长度
    int getSyntaxDistance(int word1_idx, int word2_idx) {
        int lca = getLCA(word1_idx, word2_idx);
        return depth[word1_idx] + depth[word2_idx] - 2 * depth[lca];
    }
    
    // 获取两个词的最近公共语法成分
    string getCommonSyntaxComponent(int word1_idx, int word2_idx) {
        int lca = getLCA(word1_idx, word2_idx);
        return pos[lca];  // 返回LCA节点的词性，表示公共语法成分
    }
    
    // 计算语义相似度（基于语法距离）
    double computeSemanticSimilarity(int word1_idx, int word2_idx) {
        int distance = getSyntaxDistance(word1_idx, word2_idx);
        // 距离越近，相似度越高
        return 1.0 / (1.0 + distance);
    }
};

int main() {
    // 示例：分析句子 "The cat sits on the mat"
    // 构建简化的语法依存树
    SyntaxTreeAnalyzer analyzer(6);
    
    // 添加依存关系（简化版）
    // root -> sits (动词)
    analyzer.addDependency(0, 3, "sits", "VB");  // sits 是第3个词
    
    // sits -> cat (主语)
    analyzer.addDependency(3, 2, "cat", "NN");
    
    // sits -> The (cat的定语)
    analyzer.addDependency(2, 1, "The", "DT");
    
    // sits -> on (介词短语)
    analyzer.addDependency(3, 4, "on", "IN");
    
    // on -> mat (介词宾语)
    analyzer.addDependency(4, 6, "mat", "NN");
    
    // mat -> the (mat的定语)
    analyzer.addDependency(6, 5, "the", "DT");
    
    analyzer.preprocess(0);  // 从虚拟根节点开始预处理
    
    // 查询示例
    cout << "词汇'cat'(2)和'mat'(6)的语法距离: " 
         << analyzer.getSyntaxDistance(2, 6) << endl;
    
    cout << "词汇'cat'(2)和'mat'(6)的公共语法成分: " 
         << analyzer.getCommonSyntaxComponent(2, 6) << endl;
    
    cout << "词汇'cat'(2)和'mat'(6)的语义相似度: " 
         << analyzer.computeSemanticSimilarity(2, 6) << endl;
    
    return 0;
}
```

## 2. 知识图谱中的LCA应用

### 问题2：概念层次推理

**应用场景**：
在知识图谱中，实体间的关系可以通过概念层次树的LCA来推理。

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

const int MAXN = 1e5 + 5;
const int LOG = 20;

class KnowledgeGraphLCA {
private:
    vector<int> adj[MAXN];
    unordered_map<string, int> entity_to_id;
    unordered_map<int, string> id_to_entity;
    vector<string> entity_type;  // 实体类型
    int depth[MAXN];
    int up[MAXN][LOG];
    int entity_count = 0;
    
public:
    int addEntity(const string& entity, const string& type = "") {
        if (entity_to_id.count(entity)) {
            return entity_to_id[entity];
        }
        
        int id = ++entity_count;
        entity_to_id[entity] = id;
        id_to_entity[id] = entity;
        entity_type.push_back(type);
        
        if (entity_type.size() <= id) {
            entity_type.resize(id + 1);
        }
        entity_type[id] = type;
        
        return id;
    }
    
    void addRelation(const string& parent, const string& child) {
        int pid = addEntity(parent);
        int cid = addEntity(child);
        adj[pid].push_back(cid);
    }
    
    void preprocess(int root) {
        dfs(root, 0);
    }
    
    void dfs(int u, int parent) {
        depth[u] = depth[parent] + 1;
        up[u][0] = parent;
        
        for (int k = 1; k < LOG; k++) {
            up[u][k] = up[up[u][k-1]][k-1];
        }
        
        for (int v : adj[u]) {
            dfs(v, u);
        }
    }
    
    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        
        int diff = depth[u] - depth[v];
        for (int k = 0; k < LOG; k++) {
            if (diff & (1 << k)) {
                u = up[u][k];
            }
        }
        
        if (u == v) return u;
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[u][k] != up[v][k]) {
                u = up[u][k];
                v = up[v][k];
            }
        }
        
        return up[u][0];
    }
    
    // 计算两个实体的概念距离
    int getConceptualDistance(const string& entity1, const string& entity2) {
        if (!entity_to_id.count(entity1) || !entity_to_id.count(entity2)) {
            return -1;  // 实体不存在
        }
        
        int id1 = entity_to_id[entity1];
        int id2 = entity_to_id[entity2];
        int lca = getLCA(id1, id2);
        
        return depth[id1] + depth[id2] - 2 * depth[lca];
    }
    
    // 获取两个实体的最近公共概念
    string getNearestCommonConcept(const string& entity1, const string& entity2) {
        if (!entity_to_id.count(entity1) || !entity_to_id.count(entity2)) {
            return "";
        }
        
        int id1 = entity_to_id[entity1];
        int id2 = entity_to_id[entity2];
        int lca = getLCA(id1, id2);
        
        return id_to_entity[lca];
    }
    
    // 判断实体间是否具有特定关系
    bool hasRelation(const string& entity1, const string& entity2, int maxDepthDiff = 2) {
        int dist = getConceptualDistance(entity1, entity2);
        return dist <= maxDepthDiff && dist > 0;
    }
    
    // 推理两个实体间的关系类型
    string inferRelationType(const string& entity1, const string& entity2) {
        string common_concept = getNearestCommonConcept(entity1, entity2);
        if (common_concept.empty()) return "no_relation";
        
        int id1 = entity_to_id[entity1];
        int id2 = entity_to_id[entity2];
        int lca = getLCA(id1, id2);
        
        if (lca == id1) return "is_instance_of";
        if (lca == id2) return "is_instance_of_reverse";
        
        int dist1 = depth[id1] - depth[lca];
        int dist2 = depth[id2] - depth[lca];
        
        if (dist1 == 1 && dist2 == 1) return "sibling";
        if (dist1 > dist2) return "more_specific";
        if (dist2 > dist1) return "more_general";
        
        return "cousin";
    }
};

int main() {
    KnowledgeGraphLCA kg;
    
    // 构建动物分类树
    kg.addRelation("animal", "mammal");
    kg.addRelation("animal", "bird");
    kg.addRelation("mammal", "cat");
    kg.addRelation("mammal", "dog");
    kg.addRelation("cat", "persian");
    kg.addRelation("cat", "siamese");
    kg.addRelation("bird", "eagle");
    kg.addRelation("bird", "sparrow");
    
    kg.preprocess(kg.addEntity("animal"));  // 以animal为根
    
    // 查询示例
    cout << "cat和dog的概念距离: " << kg.getConceptualDistance("cat", "dog") << endl;
    cout << "cat和dog的最近公共概念: " << kg.getNearestCommonConcept("cat", "dog") << endl;
    cout << "cat和dog的关系类型: " << kg.inferRelationType("cat", "dog") << endl;
    
    cout << "\npersian和eagle的概念距离: " << kg.getConceptualDistance("persian", "eagle") << endl;
    cout << "persian和eagle的最近公共概念: " << kg.getNearestCommonConcept("persian", "eagle") << endl;
    cout << "persian和eagle的关系类型: " << kg.inferRelationType("persian", "eagle") << endl;
    
    return 0;
}
```

## 3. 图神经网络中的LCA应用

### 问题3：层级注意力机制

**应用场景**：
在图神经网络中，利用LCA计算节点间的层级关系，构建层级注意力。

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 1e4 + 5;
const int LOG = 15;

class HierarchicalGNN {
private:
    vector<int> adj[MAXN];
    int depth[MAXN];
    int up[MAXN][LOG];
    vector<double> node_features;  // 节点特征
    vector<vector<double>> attention_weights;  // 注意力权重
    
public:
    HierarchicalGNN(int n) {
        node_features.resize(n + 1);
        attention_weights.resize(n + 1, vector<double>(n + 1, 0.0));
    }
    
    void addEdge(int u, int v) {
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    void setFeature(int node, double feature) {
        node_features[node] = feature;
    }
    
    void preprocess(int root) {
        dfs(root, 0);
        computeAttentionWeights();
    }
    
    void dfs(int u, int parent) {
        depth[u] = depth[parent] + 1;
        up[u][0] = parent;
        
        for (int k = 1; k < LOG; k++) {
            up[u][k] = up[up[u][k-1]][k-1];
        }
        
        for (int v : adj[u]) {
            if (v != parent) {
                dfs(v, u);
            }
        }
    }
    
    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        
        int diff = depth[u] - depth[v];
        for (int k = 0; k < LOG; k++) {
            if (diff & (1 << k)) {
                u = up[u][k];
            }
        }
        
        if (u == v) return u;
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[u][k] != up[v][k]) {
                u = up[u][k];
                v = up[v][k];
            }
        }
        
        return up[u][0];
    }
    
    // 计算节点间距离
    int getDistance(int u, int v) {
        int lca = getLCA(u, v);
        return depth[u] + depth[v] - 2 * depth[lca];
    }
    
    // 计算层级注意力权重
    void computeAttentionWeights() {
        int n = node_features.size() - 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    attention_weights[i][j] = 1.0;  // 自连接权重
                } else {
                    int distance = getDistance(i, j);
                    // 基于距离的注意力权重，距离越近权重越大
                    attention_weights[i][j] = exp(-distance * 0.1);
                }
            }
        }
        
        // 归一化每一行的权重
        for (int i = 1; i <= n; i++) {
            double sum = 0;
            for (int j = 1; j <= n; j++) {
                sum += attention_weights[i][j];
            }
            if (sum > 0) {
                for (int j = 1; j <= n; j++) {
                    attention_weights[i][j] /= sum;
                }
            }
        }
    }
    
    // 消息传递更新节点表示
    vector<double> messagePassing(int target_node, int iterations = 1) {
        vector<double> current_features = node_features;
        
        for (int iter = 0; iter < iterations; iter++) {
            vector<double> new_features = current_features;
            int n = current_features.size() - 1;
            
            for (int i = 1; i <= n; i++) {
                double weighted_sum = 0;
                for (int j = 1; j <= n; j++) {
                    weighted_sum += attention_weights[i][j] * current_features[j];
                }
                new_features[i] = weighted_sum;
            }
            
            current_features = new_features;
        }
        
        return current_features;
    }
    
    // 获取节点间路径上的关键节点
    vector<int> getCriticalPathNodes(int u, int v) {
        vector<int> path;
        int lca = getLCA(u, v);
        
        // 从u到LCA的路径
        int current = u;
        while (current != lca) {
            path.push_back(current);
            current = up[current][0];
        }
        path.push_back(lca);  // 添加LCA
        
        // 从LCA到v的路径（不包含LCA以避免重复）
        vector<int> temp_path;
        current = v;
        while (current != lca) {
            temp_path.push_back(current);
            current = up[current][0];
        }
        
        // 反转并添加到结果
        reverse(temp_path.begin(), temp_path.end());
        path.insert(path.end(), temp_path.begin(), temp_path.end());
        
        return path;
    }
};

int main() {
    int n = 6;
    HierarchicalGNN gnn(n);
    
    // 构建树结构
    gnn.addEdge(1, 2);
    gnn.addEdge(1, 3);
    gnn.addEdge(2, 4);
    gnn.addEdge(2, 5);
    gnn.addEdge(3, 6);
    
    // 设置节点特征
    for (int i = 1; i <= n; i++) {
        gnn.setFeature(i, i * 1.0);  // 简单的特征设置
    }
    
    gnn.preprocess(1);  // 以节点1为根
    
    // 查询示例
    cout << "节点1和节点6的距离: " << gnn.getDistance(1, 6) << endl;
    
    vector<int> critical_nodes = gnn.getCriticalPathNodes(4, 6);
    cout << "节点4到节点6的路径节点: ";
    for (int node : critical_nodes) {
        cout << node << " ";
    }
    cout << endl;
    
    // 消息传递示例
    vector<double> updated_features = gnn.messagePassing(1);
    cout << "消息传递后的节点特征: ";
    for (int i = 1; i <= n; i++) {
        cout << "节点" << i << ":" << updated_features[i] << " ";
    }
    cout << endl;
    
    return 0;
}
```

## 4. 推荐系统中的LCA应用

### 问题4：层次化推荐

**应用场景**：
在具有层次结构的推荐系统中，利用LCA计算物品间的相似度。

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

const int MAXN = 1e4 + 5;
const int LOG = 15;

class HierarchicalRecommendation {
private:
    vector<int> adj[MAXN];  // 类别树
    unordered_map<string, int> item_to_category;  // 物品到类别的映射
    unordered_map<string, double> item_popularity;  // 物品流行度
    unordered_map<int, string> category_names;  // 类别ID到名称
    int depth[MAXN];
    int up[MAXN][LOG];
    
public:
    void addCategoryRelation(const string& parent, const string& child) {
        static int id_counter = 1;
        static unordered_map<string, int> name_to_id;
        
        if (!name_to_id.count(parent)) {
            name_to_id[parent] = id_counter++;
            category_names[name_to_id[parent]] = parent;
        }
        if (!name_to_id.count(child)) {
            name_to_id[child] = id_counter++;
            category_names[name_to_id[child]] = child;
        }
        
        int pid = name_to_id[parent];
        int cid = name_to_id[child];
        adj[pid].push_back(cid);
    }
    
    void addItemToCategory(const string& item, const string& category) {
        item_to_category[item] = getCategoryID(category);
    }
    
    void setItemPopularity(const string& item, double popularity) {
        item_popularity[item] = popularity;
    }
    
    int getCategoryID(const string& category) {
        static unordered_map<string, int> name_to_id;
        static int id_counter = 1;
        
        if (!name_to_id.count(category)) {
            name_to_id[category] = id_counter++;
            category_names[name_to_id[category]] = category;
        }
        return name_to_id[category];
    }
    
    void preprocess(int root) {
        dfs(root, 0);
    }
    
    void dfs(int u, int parent) {
        depth[u] = depth[parent] + 1;
        up[u][0] = parent;
        
        for (int k = 1; k < LOG; k++) {
            up[u][k] = up[up[u][k-1]][k-1];
        }
        
        for (int v : adj[u]) {
            dfs(v, u);
        }
    }
    
    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        
        int diff = depth[u] - depth[v];
        for (int k = 0; k < LOG; k++) {
            if (diff & (1 << k)) {
                u = up[u][k];
            }
        }
        
        if (u == v) return u;
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[u][k] != up[v][k]) {
                u = up[u][k];
                v = up[v][k];
            }
        }
        
        return up[u][0];
    }
    
    // 计算两个物品的类别距离
    double getItemCategoryDistance(const string& item1, const string& item2) {
        if (!item_to_category.count(item1) || !item_to_category.count(item2)) {
            return -1;  // 物品不存在
        }
        
        int cat1 = item_to_category[item1];
        int cat2 = item_to_category[item2];
        int lca = getLCA(cat1, cat2);
        
        return (double)(depth[cat1] + depth[cat2] - 2 * depth[lca]);
    }
    
    // 计算两个物品的相似度
    double computeItemSimilarity(const string& item1, const string& item2) {
        double category_distance = getItemCategoryDistance(item1, item2);
        if (category_distance < 0) return 0.0;
        
        // 基于类别距离的相似度计算
        double category_similarity = 1.0 / (1.0 + category_distance);
        
        // 可以结合其他因素如流行度
        double pop1 = item_popularity.count(item1) ? item_popularity[item1] : 0.5;
        double pop2 = item_popularity.count(item2) ? item_popularity[item2] : 0.5;
        double popularity_similarity = 1.0 - abs(pop1 - pop2);
        
        // 综合相似度
        return 0.7 * category_similarity + 0.3 * popularity_similarity;
    }
    
    // 基于LCA的推荐
    vector<pair<string, double>> getRecommendations(const string& target_item, 
                                                   const vector<string>& candidate_items, 
                                                   int top_k = 5) {
        vector<pair<double, string>> scores;
        
        for (const string& item : candidate_items) {
            if (item != target_item) {
                double similarity = computeItemSimilarity(target_item, item);
                scores.push_back({similarity, item});
            }
        }
        
        sort(scores.rbegin(), scores.rend());
        
        vector<pair<string, double>> result;
        for (int i = 0; i < min(top_k, (int)scores.size()); i++) {
            result.push_back({scores[i].second, scores[i].first});
        }
        
        return result;
    }
};

int main() {
    HierarchicalRecommendation rec;
    
    // 构建商品类别树
    rec.addCategoryRelation("Electronics", "Computers");
    rec.addCategoryRelation("Electronics", "Phones");
    rec.addCategoryRelation("Computers", "Laptops");
    rec.addCategoryRelation("Computers", "Desktops");
    rec.addCategoryRelation("Phones", "Smartphones");
    rec.addCategoryRelation("Phones", "Feature Phones");
    
    // 添加物品到类别
    rec.addItemToCategory("MacBook Pro", "Laptops");
    rec.addItemToCategory("Dell XPS", "Laptops");
    rec.addItemToCategory("iPhone 13", "Smartphones");
    rec.addItemToCategory("Samsung Galaxy", "Smartphones");
    rec.addItemToCategory("HP Desktop", "Desktops");
    
    // 设置物品流行度
    rec.setItemPopularity("MacBook Pro", 0.9);
    rec.setItemPopularity("Dell XPS", 0.7);
    rec.setItemPopularity("iPhone 13", 0.95);
    rec.setItemPopularity("Samsung Galaxy", 0.85);
    rec.setItemPopularity("HP Desktop", 0.6);
    
    rec.preprocess(rec.getCategoryID("Electronics"));
    
    // 推荐示例
    vector<string> candidates = {"Dell XPS", "iPhone 13", "Samsung Galaxy", "HP Desktop"};
    vector<pair<string, double>> recommendations = rec.getRecommendations("MacBook Pro", candidates);
    
    cout << "基于MacBook Pro的推荐:" << endl;
    for (auto& rec : recommendations) {
        cout << "物品: " << rec.first << ", 相似度: " << rec.second << endl;
    }
    
    // 类别距离示例
    cout << "\nMacBook Pro和iPhone 13的类别距离: " 
         << rec.getItemCategoryDistance("MacBook Pro", "iPhone 13") << endl;
    
    return 0;
}
```

## 5. 生物信息学中的LCA应用

### 问题5：进化树分析

**应用场景**：
在生物信息学中，利用进化树的LCA分析物种间的进化关系。

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

const int MAXN = 1e4 + 5;
const int LOG = 15;

class PhylogeneticTreeAnalyzer {
private:
    vector<int> adj[MAXN];
    unordered_map<string, int> species_to_id;
    unordered_map<int, string> id_to_species;
    vector<double> branch_length;  // 分支长度
    int depth[MAXN];
    int up[MAXN][LOG];
    int species_count = 0;
    
public:
    int addSpecies(const string& species) {
        if (species_to_id.count(species)) {
            return species_to_id[species];
        }
        
        int id = ++species_count;
        species_to_id[species] = id;
        id_to_species[id] = species;
        
        if (branch_length.size() <= id) {
            branch_length.resize(id + 1, 1.0);  // 默认分支长度为1
        }
        
        return id;
    }
    
    void addEvolutionaryRelation(const string& ancestor, const string& descendant, double length = 1.0) {
        int aid = addSpecies(ancestor);
        int did = addSpecies(descendant);
        adj[aid].push_back(did);
        
        if (branch_length.size() <= did) {
            branch_length.resize(did + 1);
        }
        branch_length[did] = length;
    }
    
    void preprocess(int root) {
        dfs(root, 0);
    }
    
    void dfs(int u, int parent) {
        depth[u] = depth[parent] + 1;
        up[u][0] = parent;
        
        for (int k = 1; k < LOG; k++) {
            up[u][k] = up[up[u][k-1]][k-1];
        }
        
        for (int v : adj[u]) {
            dfs(v, u);
        }
    }
    
    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        
        int diff = depth[u] - depth[v];
        for (int k = 0; k < LOG; k++) {
            if (diff & (1 << k)) {
                u = up[u][k];
            }
        }
        
        if (u == v) return u;
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[u][k] != up[v][k]) {
                u = up[u][k];
                v = up[v][k];
            }
        }
        
        return up[u][0];
    }
    
    // 计算两个物种的进化距离
    double getEvolutionaryDistance(const string& species1, const string& species2) {
        if (!species_to_id.count(species1) || !species_to_id.count(species2)) {
            return -1;  // 物种不存在
        }
        
        int id1 = species_to_id[species1];
        int id2 = species_to_id[species2];
        int lca = getLCA(id1, id2);
        
        // 进化距离：到LCA的距离之和
        int dist1 = depth[id1] - depth[lca];
        int dist2 = depth[id2] - depth[lca];
        
        // 这里简化为节点数距离，实际应用中可能需要考虑分支长度
        return dist1 + dist2;
    }
    
    // 获取两个物种的最近共同祖先
    string getCommonAncestor(const string& species1, const string& species2) {
        if (!species_to_id.count(species1) || !species_to_id.count(species2)) {
            return "";
        }
        
        int id1 = species_to_id[species1];
        int id2 = species_to_id[species2];
        int lca = getLCA(id1, id2);
        
        return id_to_species[lca];
    }
    
    // 判断两个物种的进化关系
    string getEvolutionaryRelationship(const string& species1, const string& species2) {
        string ancestor = getCommonAncestor(species1, species2);
        if (ancestor.empty()) return "unknown";
        
        int id1 = species_to_id[species1];
        int id2 = species_to_id[species2];
        int lca = getLCA(id1, id2);
        
        if (lca == id1) return species2 + " is descendant of " + species1;
        if (lca == id2) return species1 + " is descendant of " + species2;
        
        // 检查是否为兄弟关系（共同祖先的直接子节点）
        if (up[id1][0] == up[id2][0]) {
            return species1 + " and " + species2 + " are siblings";
        }
        
        return species1 + " and " + species2 + " share common ancestor " + ancestor;
    }
    
    // 计算进化树的平衡性（基于LCA深度）
    double calculateTreeBalance() {
        vector<int> all_species;
        for (auto& pair : species_to_id) {
            all_species.push_back(pair.second);
        }
        
        if (all_species.size() < 2) return 1.0;  // 单节点树是平衡的
        
        double total_lca_depth = 0;
        int count = 0;
        
        for (int i = 0; i < all_species.size(); i++) {
            for (int j = i + 1; j < all_species.size(); j++) {
                int lca = getLCA(all_species[i], all_species[j]);
                total_lca_depth += depth[lca];
                count++;
            }
        }
        
        // 平衡性：LCA深度越小，树越不平衡
        return 1.0 / (1.0 + total_lca_depth / count);
    }
};

int main() {
    PhylogeneticTreeAnalyzer analyzer;
    
    // 构建简化的进化树
    analyzer.addEvolutionaryRelation("Common Ancestor", "Mammals");
    analyzer.addEvolutionaryRelation("Common Ancestor", "Birds");
    analyzer.addEvolutionaryRelation("Mammals", "Primates");
    analyzer.addEvolutionaryRelation("Mammals", "Carnivores");
    analyzer.addEvolutionaryRelation("Primates", "Apes");
    analyzer.addEvolutionaryRelation("Primates", "Monkeys");
    analyzer.addEvolutionaryRelation("Apes", "Humans");
    analyzer.addEvolutionaryRelation("Apes", "Chimpanzees");
    analyzer.addEvolutionaryRelation("Carnivores", "Cats");
    analyzer.addEvolutionaryRelation("Carnivores", "Dogs");
    
    analyzer.preprocess(analyzer.addSpecies("Common Ancestor"));
    
    // 分析示例
    cout << "Humans和Chimpanzees的进化距离: " 
         << analyzer.getEvolutionaryDistance("Humans", "Chimpanzees") << endl;
    
    cout << "Humans和Cats的最近共同祖先: " 
         << analyzer.getCommonAncestor("Humans", "Cats") << endl;
    
    cout << "Humans和Chimpanzees的进化关系: " 
         << analyzer.getEvolutionaryRelationship("Humans", "Chimpanzees") << endl;
    
    cout << "进化树平衡性: " << analyzer.calculateTreeBalance() << endl;
    
    return 0;
}
```

这些高级应用展示了LCA算法在机器学习和人工智能领域的广泛应用，包括自然语言处理、知识图谱、图神经网络、推荐系统和生物信息学等重要领域。通过这些应用，LCA算法不仅在算法竞赛中重要，在实际的AI应用中也发挥着关键作用。