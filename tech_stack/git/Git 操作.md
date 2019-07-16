# Git 操作

## 基本操作

`git init` : 将当前目录变成 Git 可管理的仓库

`ls -ah` : 查看当前目录下的所有文件，包括隐藏文件

`git add [-f] <file>` : 将文件添加到仓库，-f 强制将文件添加的 Git

`git commit -m "<desc>"` : 将文件提交到仓库

`git status` : 查看当前仓库的状态，包括文件修改状态等等

`git diff <file>` : 查看修改的内容

`git log` : 查看 Git 提交记录

`git log --graph` : 查看分支合并图

`git check-ignore -v <file>` : 检查文件是否被 ignore

`git config --global alias.<alias name> <git command>` : 为指定的 Git 命令设定别名



## 版本操作

`git reset --hard HEAD^` : 回退到上一个版本

`git reset --hard <git_version_id>` : 设置到指定版本

`git reflog [--pretty=oneline]` ：查看每一次操作的命令

`git checkout -- <file>` : 将工作区文件撤回为添加到暂存区之后的状态

`git reset HEAD <file>` : 将暂存区的修改回退到工作区

结合 ` git reset HEAD <file>`  & `git checkout -- <file>` 就可以将暂存区的文件修改内容撤回了

`git rm <file>` & `git commit -m <desc>` : 版本库中删除文件



## 远程仓库操作

`git remote add origin <remote url>` : 关联远程仓库

`git push -u origin master` : 将本地库内容推送至远程仓库，第一次推送时，加上 -u 会将本地master 和 远程 master 关联起来

`git clone <remote url>` : 将远程库克隆到本地



`git remote add <upstream> <remote url>`  &  `git fetch <upstream>` & `git merge <upstream>/<master>` & `git push orgin/master` : 同步fork 项目，于原作者保持一致



## 分支操作

`git checkout -b <branch name>` : 创建并且切换至新的分支

或者 `git branch <branch name>`  + `git checkout <branch name>`

`git branch` : 查看所有分支

`git merge <branch name>` : 合并指定分支至当前分支

`git branch -d <branch name>` : 删除指定分支 

` git branch -D <branch name>` : 强制删除指定分支（当前分支未合并到其他分支的情况下使用）

`git merge --no-ff -m <desc> <branch name>` : 合并指定分支到当前分支，保留之前分支历史记录

`git stash` : 把当前工作现场保存起来

`git stash list` : 列出当前分支保存的现场

`git stash pop`  or `git stash apply <stash id> ` + `git stash drop <stash id>` : 恢复现场

`git remote [-v]` : 查看远程仓库信息

`git push origin <branch name>` : 推送分支到远程仓库

`git rebase -i [start index] [end index]` : 变基操作（左开右闭）

> p,pick = use commit （保留该commit）
>
> r,reword = use commit, but edit the commit message （保留该commit，但修改提交的注释信息）
>
> e,edit = use commit, but stop for amending （保留该commit，但要停下来修改）
>
> s,squash = use commit, but meld into previous commit （将该commit 和前一个 commit 合并）
>
> f,fixup = like 'squash', but discard this commit's log message （和 squash类似，但不保留提交的注释信息）
>
> x, exec = run command(the rest of the line) using shell （执行shell 命令）
>
> d,drop = remove commit （移除该commit）

`git rebase --onto <branch name> [start index] [end index] ` + `git reset --hard end index` : 将branch 分支中指定的 commits 复制到当前分支

`git cherry-pick [commit id 1] [commit id 2] ...` : 和上面的效果一致，只是需要列出所有需要复制的commit id



## 标签管理

`git tag <tag name>` [commit id] : 给指定的 commit id 所对应位置创建 tag

`git tag -a <tag name> -m <message> [commit id]` : 与上面一致，-a 指定标签名， -m 指定说明文字

`git tag` : 列出所有的 tag

`git show <tag name>` : 查看 tag 信息

`git tag -d <tag name>` : 删除指定的 tag

`git push origin <tag name>` : 将 tag 推送至远程仓库

`git push origin --tags` : 将所有 tag 推送至远程仓库

`git push origin :/refs/tags/<tag name>` : 本地删除了 tag， 将远程对应的 tag 删除



## 实践

`git remote add <remote_name> <remote_address>`  : 添加新的remote 信息

