// LeetCode 582. Kill Process (杀死进程)
// 来源: LeetCode
// 网址: https://leetcode.cn/problems/kill-process/
// 
// 题目描述:
// 给定n个进程，每个进程都有一个唯一的PID（进程ID）和它的PPID（父进程ID）。
// 每个进程只有一个父进程，但是可能有多个子进程。这形成了一个树状结构。
// 只有一个进程的PPID是0，这意味着这个进程没有父进程。所有的进程都应该是这个进程的后代。
// 当一个进程被杀死时，它的所有子进程和后代进程也应该被杀死。
// 给定一个PID和一个PPID列表，以及一个要杀死的进程kill，请返回所有应该被杀死的进程的ID列表。
// 你可以以任意顺序返回答案。
// 
// 示例:
// 输入：
// pid = [1, 3, 10, 5]
// ppid = [3, 0, 5, 3]
// kill = 5
// 输出：[5, 10]
// 解释：
//           3
//         /   \
//        1     5
//             /
//            10
// 杀死进程5，其子进程10也应该被杀死。
// 
// 解题思路:
// 1. 首先构建进程之间的父子关系映射
// 2. 使用深度优先搜索（递归）从kill进程开始，收集所有应该被杀死的进程
// 
// 时间复杂度: O(n)，其中n是进程的数量，构建映射需要O(n)，DFS遍历需要O(n)
// 空间复杂度: O(n)，用于存储映射和递归调用栈

#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
using namespace std;

class Solution {
public:
    // 使用深度优先搜索解决问题
    vector<int> killProcess(vector<int>& pid, vector<int>& ppid, int kill) {
        // 结果列表，存储所有应该被杀死的进程ID
        vector<int> result;
        
        // 构建父进程到子进程的映射
        unordered_map<int, vector<int>> parentToChildren = buildProcessTree(pid, ppid);
        
        // 从kill进程开始，递归收集所有应该被杀死的进程
        dfs(parentToChildren, kill, result);
        
        return result;
    }
    
    // 使用广度优先搜索解决问题（迭代方法）
    vector<int> killProcessBFS(vector<int>& pid, vector<int>& ppid, int kill) {
        // 结果列表，存储所有应该被杀死的进程ID
        vector<int> result;
        
        // 构建父进程到子进程的映射
        unordered_map<int, vector<int>> parentToChildren = buildProcessTree(pid, ppid);
        
        // 使用队列进行广度优先搜索
        queue<int> q;
        q.push(kill);
        
        while (!q.empty()) {
            int currentProcess = q.front();
            q.pop();
            result.push_back(currentProcess);
            
            // 将当前进程的所有子进程加入队列
            auto it = parentToChildren.find(currentProcess);
            if (it != parentToChildren.end()) {
                for (int child : it->second) {
                    q.push(child);
                }
            }
        }
        
        return result;
    }
private:
    /**
     * 构建父进程到子进程的映射
     * @param pid 进程ID列表
     * @param ppid 父进程ID列表
     * @return 父进程到子进程的映射
     */
    unordered_map<int, vector<int>> buildProcessTree(vector<int>& pid, vector<int>& ppid) {
        unordered_map<int, vector<int>> parentToChildren;
        
        // 遍历所有进程，构建父子关系
        for (int i = 0; i < pid.size(); i++) {
            int parentId = ppid[i];
            int childId = pid[i];
            
            // 将子进程添加到父进程的子列表中
            parentToChildren[parentId].push_back(childId);
        }
        
        return parentToChildren;
    }
    
    /**
     * 深度优先搜索，收集所有应该被杀死的进程
     * @param parentToChildren 父进程到子进程的映射
     * @param currentProcess 当前处理的进程ID
     * @param result 结果列表
     */
    void dfs(unordered_map<int, vector<int>>& parentToChildren, int currentProcess, vector<int>& result) {
        // 将当前进程添加到结果列表中（标记为需要被杀死）
        result.push_back(currentProcess);
        
        // 获取当前进程的所有子进程
        auto it = parentToChildren.find(currentProcess);
        if (it != parentToChildren.end()) {
            // 递归处理每个子进程
            for (int child : it->second) {
                dfs(parentToChildren, child, result);
            }
        }
    }
};

// 打印向量辅助函数
void printVector(const vector<int>& vec) {
    cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        cout << vec[i];
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]";
}

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> pid1 = {1, 3, 10, 5};
    vector<int> ppid1 = {3, 0, 5, 3};
    int kill1 = 5;
    
    cout << "测试用例1 (DFS方法):" << endl;
    cout << "pid = [1, 3, 10, 5]" << endl;
    cout << "ppid = [3, 0, 5, 3]" << endl;
    cout << "kill = 5" << endl;
    cout << "输出: ";
    printVector(solution.killProcess(pid1, ppid1, kill1));
    cout << endl;
    cout << "期望: [5, 10]" << endl;
    cout << endl;
    
    cout << "测试用例1 (BFS方法):" << endl;
    cout << "输出: ";
    printVector(solution.killProcessBFS(pid1, ppid1, kill1));
    cout << endl;
    cout << "期望: [5, 10]" << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> pid2 = {1};
    vector<int> ppid2 = {0};
    int kill2 = 1;
    
    cout << "测试用例2 (DFS方法):" << endl;
    cout << "pid = [1]" << endl;
    cout << "ppid = [0]" << endl;
    cout << "kill = 1" << endl;
    cout << "输出: ";
    printVector(solution.killProcess(pid2, ppid2, kill2));
    cout << endl;
    cout << "期望: [1]" << endl;
    cout << endl;
    
    // 测试用例3：更复杂的树结构
    vector<int> pid3 = {1, 2, 3, 4, 5, 6, 7};
    vector<int> ppid3 = {0, 1, 1, 2, 2, 3, 3};
    int kill3 = 2;
    
    cout << "测试用例3 (DFS方法):" << endl;
    cout << "pid = [1, 2, 3, 4, 5, 6, 7]" << endl;
    cout << "ppid = [0, 1, 1, 2, 2, 3, 3]" << endl;
    cout << "kill = 2" << endl;
    cout << "输出: ";
    printVector(solution.killProcess(pid3, ppid3, kill3));
    cout << endl;
    // 期望: [2, 4, 5]
    cout << "期望: [2, 4, 5]" << endl;
    
    return 0;
}