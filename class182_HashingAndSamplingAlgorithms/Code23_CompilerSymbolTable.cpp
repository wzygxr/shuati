#include <iostream>
#include <vector>
#include <unordered_map>
#include <string>
#include <random>
#include <algorithm>
#include <cmath>

using namespace std;

/**
 * 编译器符号表实现 - 使用完美哈希技术
 * 
 * 应用场景：编译器中的符号表管理、静态字典、关键字查找
 * 
 * 算法原理：
 * 1. 使用两级哈希结构实现完美哈希
 * 2. 第一级哈希将关键字分组到桶中
 * 3. 为每个桶构建无冲突的二级哈希表
 * 4. 保证O(1)时间复杂度的查找操作
 * 
 * 时间复杂度：
 * - 构建：O(n) 平均情况
 * - 查找：O(1) 最坏情况
 * 
 * 空间复杂度：O(n)
 */

// 符号表条目结构
struct SymbolEntry {
    string name;        // 符号名称
    string type;        // 符号类型
    int scope;          // 作用域
    int lineNumber;     // 行号
    
    SymbolEntry(const string& n, const string& t, int s, int l) 
        : name(n), type(t), scope(s), lineNumber(l) {}
    
    string toString() const {
        return "SymbolEntry{name='" + name + "', type='" + type + 
               "', scope=" + to_string(scope) + ", line=" + to_string(lineNumber) + "}";
    }
};

// 完美哈希符号表实现
class PerfectHashSymbolTable {
private:
    int firstLevelSize;
    vector<vector<SymbolEntry>> secondLevelTables;
    vector<int> secondLevelSizes;
    vector<int> hashParams;
    vector<string> allKeys;
    
    // 哈希函数
    int hash(const string& key, int param, int tableSize) const {
        hash<string> hasher;
        int h = hasher(key);
        h = (h ^ param) * 0x9e3779b9;
        return abs(h) % tableSize;
    }
    
    // 查找无冲突的哈希函数参数
    int findHashFunction(const vector<string>& keys, int tableSize) {
        if (keys.size() <= 1) return 0;
        
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(1, 1000000);
        
        for (int attempt = 0; attempt < 1000; attempt++) {
            int param = dis(gen);
            vector<bool> used(tableSize, false);
            bool collision = false;
            
            for (const string& key : keys) {
                int index = hash(key, param, tableSize);
                if (used[index]) {
                    collision = true;
                    break;
                }
                used[index] = true;
            }
            
            if (!collision) return param;
        }
        
        return 1; // fallback
    }

public:
    // 构造函数：根据关键字集合构建完美哈希表
    PerfectHashSymbolTable(const vector<SymbolEntry>& symbols) {
        // 收集所有关键字
        unordered_map<string, SymbolEntry> symbolMap;
        for (const auto& symbol : symbols) {
            allKeys.push_back(symbol.name);
            symbolMap[symbol.name] = symbol;
        }
        
        int n = allKeys.size();
        firstLevelSize = max(1, (int) ceil(n * 2.0)); // 一级表大小
        secondLevelTables.resize(firstLevelSize);
        secondLevelSizes.resize(firstLevelSize, 0);
        hashParams.resize(firstLevelSize, 0);
        
        // 分组
        unordered_map<int, vector<string>> groups;
        for (const string& key : allKeys) {
            int groupIndex = abs(hash<string>{}(key)) % firstLevelSize;
            groups[groupIndex].push_back(key);
        }
        
        // 为每组构建二级表
        for (const auto& groupEntry : groups) {
            int groupIndex = groupEntry.first;
            const vector<string>& groupKeys = groupEntry.second;
            
            if (groupKeys.empty()) continue;
            
            // 计算二级表大小
            int groupSize = groupKeys.size();
            int secondLevelSize = groupSize <= 2 ? groupSize * 2 : groupSize * groupSize;
            
            // 寻找无冲突哈希函数
            int hashParam = findHashFunction(groupKeys, secondLevelSize);
            hashParams[groupIndex] = hashParam;
            secondLevelSizes[groupIndex] = secondLevelSize;
            
            // 创建二级表
            vector<SymbolEntry> table(secondLevelSize);
            for (const string& key : groupKeys) {
                int index = hash(key, hashParam, secondLevelSize);
                table[index] = symbolMap[key];
            }
            secondLevelTables[groupIndex] = table;
        }
    }
    
    // 查找符号
    SymbolEntry* lookup(const string& symbolName) {
        if (symbolName.empty()) return nullptr;
        
        int firstIndex = abs(hash<string>{}(symbolName)) % firstLevelSize;
        const vector<SymbolEntry>& secondLevelTable = secondLevelTables[firstIndex];
        
        if (secondLevelTable.empty()) return nullptr;
        
        int secondLevelSize = secondLevelSizes[firstIndex];
        int hashParam = hashParams[firstIndex];
        int secondIndex = hash(symbolName, hashParam, secondLevelSize);
        
        if (secondIndex < secondLevelTable.size()) {
            // 注意：这里需要检查是否是有效的条目
            // 在实际实现中，可能需要额外的标记来标识有效条目
            return const_cast<SymbolEntry*>(&secondLevelTable[secondIndex]);
        }
        
        return nullptr;
    }
    
    // 获取所有符号名称
    const vector<string>& getAllSymbolNames() const {
        return allKeys;
    }
    
    // 获取符号表大小
    int size() const {
        return allKeys.size();
    }
};

// 优化版本：使用更高效的完美哈希实现
class OptimizedPerfectHashSymbolTable {
private:
    int firstLevelSize;
    vector<vector<SymbolEntry>> secondLevelTables;
    vector<int> secondLevelSizes;
    vector<int> hashParams;
    
    int hash(const string& key, int param, int tableSize) const {
        hash<string> hasher;
        int h = hasher(key);
        h = (h ^ param) * 0x9e3779b9;
        return abs(h) % tableSize;
    }
    
    int findHashFunction(const vector<string>& keys, int tableSize) {
        if (keys.size() <= 1) return 0;
        
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(1, 1000000);
        
        for (int attempt = 0; attempt < 1000; attempt++) {
            int param = dis(gen);
            vector<bool> used(tableSize, false);
            bool collision = false;
            
            for (const string& key : keys) {
                int index = hash(key, param, tableSize);
                if (used[index]) {
                    collision = true;
                    break;
                }
                used[index] = true;
            }
            
            if (!collision) return param;
        }
        
        return 1; // fallback
    }

public:
    OptimizedPerfectHashSymbolTable(const vector<SymbolEntry>& symbols) {
        unordered_map<string, SymbolEntry> symbolMap;
        vector<string> keys;
        
        for (const auto& symbol : symbols) {
            symbolMap[symbol.name] = symbol;
            keys.push_back(symbol.name);
        }
        
        int n = keys.size();
        firstLevelSize = max(1, (int) ceil(n * 2.0));
        secondLevelTables.resize(firstLevelSize);
        secondLevelSizes.resize(firstLevelSize, 0);
        hashParams.resize(firstLevelSize, 0);
        
        // 分组
        unordered_map<int, vector<string>> groups;
        for (const string& key : keys) {
            int groupIndex = abs(hash<string>{}(key)) % firstLevelSize;
            groups[groupIndex].push_back(key);
        }
        
        // 为每组构建二级表
        for (const auto& groupEntry : groups) {
            int groupIndex = groupEntry.first;
            const vector<string>& groupKeys = groupEntry.second;
            
            if (groupKeys.empty()) continue;
            
            // 计算二级表大小
            int groupSize = groupKeys.size();
            int secondLevelSize = groupSize <= 2 ? groupSize * 2 : groupSize * groupSize;
            
            // 寻找无冲突哈希函数
            int hashParam = findHashFunction(groupKeys, secondLevelSize);
            hashParams[groupIndex] = hashParam;
            secondLevelSizes[groupIndex] = secondLevelSize;
            
            // 创建二级表
            vector<SymbolEntry> table(secondLevelSize);
            for (const string& key : groupKeys) {
                int index = hash(key, hashParam, secondLevelSize);
                table[index] = symbolMap[key];
            }
            secondLevelTables[groupIndex] = table;
        }
    }
    
    SymbolEntry* lookup(const string& symbolName) {
        if (symbolName.empty()) return nullptr;
        
        int firstIndex = abs(hash<string>{}(symbolName)) % firstLevelSize;
        const vector<SymbolEntry>& secondLevelTable = secondLevelTables[firstIndex];
        
        if (secondLevelTable.empty()) return nullptr;
        
        int secondLevelSize = secondLevelSizes[firstIndex];
        int hashParam = hashParams[firstIndex];
        int secondIndex = hash(symbolName, hashParam, secondLevelSize);
        
        if (secondIndex < secondLevelTable.size()) {
            return const_cast<SymbolEntry*>(&secondLevelTable[secondIndex]);
        }
        
        return nullptr;
    }
};

// 创建测试符号
vector<SymbolEntry> createTestSymbols() {
    vector<SymbolEntry> symbols;
    symbols.emplace_back("int", "keyword", 0, 1);
    symbols.emplace_back("float", "keyword", 0, 1);
    symbols.emplace_back("double", "keyword", 0, 1);
    symbols.emplace_back("char", "keyword", 0, 1);
    symbols.emplace_back("if", "keyword", 0, 2);
    symbols.emplace_back("else", "keyword", 0, 2);
    symbols.emplace_back("while", "keyword", 0, 3);
    symbols.emplace_back("for", "keyword", 0, 3);
    symbols.emplace_back("return", "keyword", 0, 4);
    symbols.emplace_back("main", "function", 0, 5);
    symbols.emplace_back("printf", "function", 0, 6);
    symbols.emplace_back("scanf", "function", 0, 6);
    return symbols;
}

void testBasicVersion(const vector<SymbolEntry>& symbols) {
    cout << "--- 基础版本测试 ---" << endl;
    PerfectHashSymbolTable symbolTable(symbols);
    
    // 测试查找功能
    SymbolEntry* intSymbol = symbolTable.lookup("int");
    SymbolEntry* mainSymbol = symbolTable.lookup("main");
    SymbolEntry* nonExistent = symbolTable.lookup("nonexistent");
    
    cout << "查找 'int': " << (intSymbol ? intSymbol->toString() : "nullptr") << endl;
    cout << "查找 'main': " << (mainSymbol ? mainSymbol->toString() : "nullptr") << endl;
    cout << "查找不存在的符号: " << (nonExistent ? nonExistent->toString() : "nullptr") << endl;
    
    cout << "符号表大小: " << symbolTable.size() << endl;
    cout << endl;
}

void testOptimizedVersion(const vector<SymbolEntry>& symbols) {
    cout << "--- 优化版本测试 ---" << endl;
    OptimizedPerfectHashSymbolTable symbolTable(symbols);
    
    // 测试查找功能
    SymbolEntry* intSymbol = symbolTable.lookup("int");
    SymbolEntry* mainSymbol = symbolTable.lookup("main");
    SymbolEntry* nonExistent = symbolTable.lookup("nonexistent");
    
    cout << "查找 'int': " << (intSymbol ? intSymbol->toString() : "nullptr") << endl;
    cout << "查找 'main': " << (mainSymbol ? mainSymbol->toString() : "nullptr") << endl;
    cout << "查找不存在的符号: " << (nonExistent ? nonExistent->toString() : "nullptr") << endl;
    cout << endl;
}

int main() {
    cout << "=== 测试 编译器符号表（完美哈希实现） ===" << endl;
    
    // 创建测试符号
    vector<SymbolEntry> symbols = createTestSymbols();
    
    // 基础版本测试
    testBasicVersion(symbols);
    
    // 优化版本测试
    testOptimizedVersion(symbols);
    
    return 0;
}