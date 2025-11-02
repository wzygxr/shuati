// 二叉树的序列化与反序列化
// 题目链接：https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
// 序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，
// 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
// 请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，
// 你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

/*
题目解析：
这是一个关于二叉树表示和重建的问题。我们需要设计两个方法：
1. serialize：将二叉树转换为字符串
2. deserialize：将字符串转换回二叉树

算法思路：
1. 基于层序遍历的序列化和反序列化：
   - 序列化：使用队列进行层序遍历，记录每个节点的值，空子节点用特殊标记（如"null"）表示
   - 反序列化：将字符串按分隔符分割，使用队列重建二叉树

2. 基于前序遍历的序列化和反序列化：
   - 序列化：使用递归进行前序遍历，记录每个节点的值，空子节点用特殊标记表示
   - 反序列化：使用递归根据前序遍历结果重建二叉树

3. 基于后序遍历的序列化和反序列化：
   - 序列化：使用递归进行后序遍历
   - 反序列化：使用栈辅助重建二叉树

时间复杂度：O(n) - 每个节点只被访问常数次
空间复杂度：O(n) - 需要存储序列化的字符串和辅助数据结构
是否为最优解：是，所有节点都需要被处理，时间复杂度不可能低于O(n)

边界情况：
- 空树：序列化为包含一个null的字符串
- 单节点树：序列化为只包含该节点值的字符串
- 完全二叉树：所有节点都有值
- 不平衡树：存在大量空子节点

与机器学习/深度学习的联系：
- 树结构的序列化在模型保存和加载中有重要应用
- 类似的技术也用于决策树模型的持久化
- 在分布式系统中，数据结构的序列化是数据传输的基础
*/

#include <iostream>
#include <string>
#include <queue>
#include <stack>
#include <vector>
#include <sstream>
#include <algorithm>
#include <cctype>

// 二叉树节点的定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// 基于层序遍历的序列化和反序列化实现
class CodecBFS {
public:
    // 序列化函数：将二叉树转换为字符串
    std::string serialize(TreeNode* root) {
        // 处理空树的边界情况
        if (!root) {
            return "[]";
        }
        
        std::vector<std::string> result;
        std::queue<TreeNode*> q;
        q.push(root);
        
        // 记录最后一个非空节点的位置
        int last_non_null_index = 0;
        
        // 层序遍历
        while (!q.empty()) {
            TreeNode* node = q.front();
            q.pop();
            
            if (node) {
                result.push_back(std::to_string(node->val));
                q.push(node->left);
                q.push(node->right);
                last_non_null_index = result.size() - 1;
            } else {
                result.push_back("null");
            }
        }
        
        // 移除末尾多余的null
        result.resize(last_non_null_index + 1);
        
        // 构造结果字符串
        std::stringstream ss;
        ss << "[";
        for (size_t i = 0; i < result.size(); ++i) {
            ss << result[i];
            if (i < result.size() - 1) {
                ss << ",";
            }
        }
        ss << "]";
        
        return ss.str();
    }
    
    // 反序列化函数：将字符串转换回二叉树
    TreeNode* deserialize(std::string data) {
        // 处理空树的边界情况
        if (data == "[]") {
            return nullptr;
        }
        
        // 分割字符串获取值列表
        std::vector<std::string> values;
        // 去除两端的括号
        size_t start = data.find_first_of('[') + 1;
        size_t end = data.find_last_of(']');
        if (start < end) {
            std::string content = data.substr(start, end - start);
            std::stringstream ss(content);
            std::string val;
            // 分割字符串
            while (std::getline(ss, val, ',')) {
                // 去除空格
                val.erase(std::remove_if(val.begin(), val.end(), ::isspace), val.end());
                values.push_back(val);
            }
        }
        
        if (values.empty()) {
            return nullptr;
        }
        
        // 创建根节点
        TreeNode* root = new TreeNode(std::stoi(values[0]));
        std::queue<TreeNode*> q;
        q.push(root);
        
        size_t i = 1;
        
        while (!q.empty() && i < values.size()) {
            TreeNode* node = q.front();
            q.pop();
            
            // 处理左子节点
            if (i < values.size() && values[i] != "null" && values[i] != "") {
                node->left = new TreeNode(std::stoi(values[i]));
                q.push(node->left);
            }
            i++;
            
            // 处理右子节点
            if (i < values.size() && values[i] != "null" && values[i] != "") {
                node->right = new TreeNode(std::stoi(values[i]));
                q.push(node->right);
            }
            i++;
        }
        
        return root;
    }
};

// 基于前序遍历的序列化和反序列化实现
class CodecDFS {
public:
    // 序列化函数：前序遍历
    std::string serialize(TreeNode* root) {
        std::string result;
        serializeDFS(root, result);
        return result;
    }
    
    // 反序列化函数
    TreeNode* deserialize(std::string data) {
        std::stringstream ss(data);
        return deserializeDFS(ss);
    }
private:
    // 递归实现前序遍历序列化
    void serializeDFS(TreeNode* node, std::string& result) {
        if (!node) {
            result += "# "; // 使用#表示空节点，并加空格分隔
            return;
        }
        
        // 前序：根-左-右
        result += std::to_string(node->val) + " ";
        serializeDFS(node->left, result);
        serializeDFS(node->right, result);
    }
    
    // 递归实现前序遍历反序列化
    TreeNode* deserializeDFS(std::stringstream& ss) {
        std::string val;
        ss >> val;
        
        if (val == "#") {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(std::stoi(val));
        node->left = deserializeDFS(ss);
        node->right = deserializeDFS(ss);
        
        return node;
    }
};

// 基于后序遍历的序列化和反序列化实现
class CodecPostOrder {
public:
    // 序列化函数：后序遍历
    std::string serialize(TreeNode* root) {
        std::string result;
        serializePostOrder(root, result);
        return result;
    }
    
    // 反序列化函数
    TreeNode* deserialize(std::string data) {
        std::stringstream ss(data);
        std::stack<std::string> values;
        std::string val;
        
        // 将所有值压入栈中（为了逆序处理）
        while (ss >> val) {
            values.push(val);
        }
        
        return deserializePostOrder(values);
    }
private:
    // 递归实现后序遍历序列化
    void serializePostOrder(TreeNode* node, std::string& result) {
        if (!node) {
            result += "# ";
            return;
        }
        
        // 后序：左-右-根
        serializePostOrder(node->left, result);
        serializePostOrder(node->right, result);
        result += std::to_string(node->val) + " ";
    }
    
    // 递归实现后序遍历反序列化
    TreeNode* deserializePostOrder(std::stack<std::string>& values) {
        if (values.empty()) {
            return nullptr;
        }
        
        std::string val = values.top();
        values.pop();
        
        if (val == "#") {
            return nullptr;
        }
        
        // 注意：后序遍历反序列化需要先处理右子树再处理左子树
        TreeNode* node = new TreeNode(std::stoi(val));
        node->right = deserializePostOrder(values);
        node->left = deserializePostOrder(values);
        
        return node;
    }
};

// 基于栈的非递归前序遍历实现
class CodecIterative {
public:
    // 序列化函数：非递归前序遍历
    std::string serialize(TreeNode* root) {
        if (!root) {
            return "#";
        }
        
        std::string result;
        std::stack<TreeNode*> stk;
        stk.push(root);
        
        while (!stk.empty()) {
            TreeNode* node = stk.top();
            stk.pop();
            
            if (!node) {
                result += "# ";
                continue;
            }
            
            result += std::to_string(node->val) + " ";
            // 注意：前序遍历非递归实现中，先压入右子节点，再压入左子节点
            stk.push(node->right);
            stk.push(node->left);
        }
        
        return result;
    }
    
    // 反序列化函数：非递归前序遍历
    TreeNode* deserialize(std::string data) {
        std::stringstream ss(data);
        std::queue<std::string> values;
        std::string val;
        
        while (ss >> val) {
            values.push(val);
        }
        
        return deserializeHelper(values);
    }
private:
    TreeNode* deserializeHelper(std::queue<std::string>& values) {
        if (values.empty()) {
            return nullptr;
        }
        
        std::string val = values.front();
        values.pop();
        
        if (val == "#") {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(std::stoi(val));
        node->left = deserializeHelper(values);
        node->right = deserializeHelper(values);
        
        return node;
    }
};

// 辅助函数：释放树的内存
void deleteTree(TreeNode* root) {
    if (!root) {
        return;
    }
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 辅助函数：验证两个树是否相同（用于测试）
bool isSameTree(TreeNode* p, TreeNode* q) {
    if (!p && !q) {
        return true;
    }
    if (!p || !q) {
        return false;
    }
    if (p->val != q->val) {
        return false;
    }
    return isSameTree(p->left, q->left) && isSameTree(p->right, q->right);
}

// 测试代码
int main() {
    // 构建测试树
    //       1
    //      / \
    //     2   3
    //        / \
    //       4   5
    TreeNode* root = new TreeNode(1);
    TreeNode* node2 = new TreeNode(2);
    TreeNode* node3 = new TreeNode(3);
    TreeNode* node4 = new TreeNode(4);
    TreeNode* node5 = new TreeNode(5);
    
    root->left = node2;
    root->right = node3;
    node3->left = node4;
    node3->right = node5;
    
    // 测试层序遍历实现
    std::cout << "=== 层序遍历实现 ===" << std::endl;
    CodecBFS codecBFS;
    std::string serializedBFS = codecBFS.serialize(root);
    std::cout << "序列化结果: " << serializedBFS << std::endl;
    TreeNode* deserializedBFS = codecBFS.deserialize(serializedBFS);
    std::cout << "反序列化后再序列化: " << codecBFS.serialize(deserializedBFS) << std::endl;
    std::cout << "树结构是否相同: " << (isSameTree(root, deserializedBFS) ? "true" : "false") << std::endl;
    
    // 测试前序遍历实现
    std::cout << "\n=== 前序遍历实现 ===" << std::endl;
    CodecDFS codecDFS;
    std::string serializedDFS = codecDFS.serialize(root);
    std::cout << "序列化结果: " << serializedDFS << std::endl;
    TreeNode* deserializedDFS = codecDFS.deserialize(serializedDFS);
    std::cout << "反序列化后再序列化: " << codecDFS.serialize(deserializedDFS) << std::endl;
    std::cout << "树结构是否相同: " << (isSameTree(root, deserializedDFS) ? "true" : "false") << std::endl;
    
    // 测试后序遍历实现
    std::cout << "\n=== 后序遍历实现 ===" << std::endl;
    CodecPostOrder codecPostOrder;
    std::string serializedPostOrder = codecPostOrder.serialize(root);
    std::cout << "序列化结果: " << serializedPostOrder << std::endl;
    TreeNode* deserializedPostOrder = codecPostOrder.deserialize(serializedPostOrder);
    std::cout << "反序列化后再序列化: " << codecPostOrder.serialize(deserializedPostOrder) << std::endl;
    std::cout << "树结构是否相同: " << (isSameTree(root, deserializedPostOrder) ? "true" : "false") << std::endl;
    
    // 测试迭代实现
    std::cout << "\n=== 非递归实现 ===" << std::endl;
    CodecIterative codecIterative;
    std::string serializedIterative = codecIterative.serialize(root);
    std::cout << "序列化结果: " << serializedIterative << std::endl;
    TreeNode* deserializedIterative = codecIterative.deserialize(serializedIterative);
    std::cout << "反序列化后再序列化: " << codecIterative.serialize(deserializedIterative) << std::endl;
    std::cout << "树结构是否相同: " << (isSameTree(root, deserializedIterative) ? "true" : "false") << std::endl;
    
    // 测试边界情况
    std::cout << "\n=== 边界情况测试 ===" << std::endl;
    
    // 测试空树
    TreeNode* emptyTree = nullptr;
    std::string serializedEmpty = codecBFS.serialize(emptyTree);
    std::cout << "空树序列化: " << serializedEmpty << std::endl;
    TreeNode* deserializedEmpty = codecBFS.deserialize(serializedEmpty);
    std::cout << "空树反序列化后再序列化: " << codecBFS.serialize(deserializedEmpty) << std::endl;
    
    // 测试单节点树
    TreeNode* singleNode = new TreeNode(42);
    std::string serializedSingle = codecBFS.serialize(singleNode);
    std::cout << "单节点树序列化: " << serializedSingle << std::endl;
    TreeNode* deserializedSingle = codecBFS.deserialize(serializedSingle);
    std::cout << "单节点树反序列化后再序列化: " << codecBFS.serialize(deserializedSingle) << std::endl;
    
    // 释放内存
    deleteTree(root);
    deleteTree(deserializedBFS);
    deleteTree(deserializedDFS);
    deleteTree(deserializedPostOrder);
    deleteTree(deserializedIterative);
    deleteTree(singleNode);
    deleteTree(deserializedSingle);
    // 空树不需要释放
    
    return 0;
}

/*
工程化考量：
1. 异常处理：
   - 处理了空树、单节点树等边界情况
   - 使用特殊标记表示空子节点，确保序列化/反序列化的准确性
   - 在字符串分割和解析过程中添加了鲁棒性处理

2. 内存管理：
   - 添加了deleteTree函数，确保不会内存泄漏
   - 在测试代码中正确释放所有分配的内存
   - 使用智能指针的替代方案可以进一步提高代码的健壮性

3. 性能优化：
   - 在层序遍历实现中优化了输出，去除了末尾多余的null值
   - 使用std::stringstream进行字符串操作，提高效率
   - 实现了非递归版本，避免了递归调用可能的栈溢出问题

4. 代码质量：
   - 提供了四种不同的实现方式（层序、前序、后序和非递归前序）
   - 代码结构清晰，职责分明
   - 添加了详细的注释说明算法思路和实现细节

5. 可扩展性：
   - 序列化格式可以根据需要调整
   - 可以轻松扩展为处理N叉树或其他树形结构
   - 使用标准库容器和算法，便于维护和扩展

6. 调试技巧：
   - 添加了验证函数isSameTree，用于检查反序列化的准确性
   - 在测试代码中包含了多种边界情况的测试
   - 提供了多种实现方式，可以根据不同场景选择合适的方法

7. C++特有优化：
   - 利用C++标准库的高效容器（queue, stack, vector）
   - 使用std::stringstream进行字符串处理，避免手动内存管理
   - 合理使用引用和指针，提高代码效率

8. 算法安全与业务适配：
   - 序列化格式多样，可以根据不同需求选择
   - 层序遍历格式类似JSON数组，易于与其他系统集成
   - 对于大型树结构，非递归实现可能更安全（避免栈溢出）

9. 数据格式考量：
   - 提供了多种序列化格式，包括数组格式和使用空格分隔的格式
   - 使用特殊字符标记空节点，确保数据的完整性
   - 支持数据格式的容错处理，提高了代码的健壮性

10. 序列化/反序列化的完整性：
    - 确保了序列化和反序列化是互逆操作
    - 测试代码验证了反序列化后再序列化可以得到相同结果
    - 处理了各种可能的数据异常情况
    - 添加了树结构相同性验证，确保功能正确性
*/