Quick start:
1) cp config/deepseek.env.example config/deepseek.env && fill keys
2) source config/deepseek.env
3) tools/agentctl plan "Fix current CI failure"
4) tools/agentctl apply --review
5) tools/agentctl verify
6) tools/agentctl push "[agent] fix CI"
7) tools/agentctl ci view latest
