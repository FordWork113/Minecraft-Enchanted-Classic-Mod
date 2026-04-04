::@echo off
chcp 65001
title Backup source code
cd C:\Program Files\7-Zip
7z a -tzip C:\Files\Projects\rmcp\enchanted-classic\backup\src-backup-client-.zip "C:\Files\Projects\rmcp\enchanted-classic\minecraft\src\"
echo Backup making!
explorer C:\Files\Projects\rmcp\enchanted-classic\backup