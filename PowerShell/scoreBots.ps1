Set-Location -Path 'PowerShell'
Remove-Item -Path 'yahtzeebot-submissions' -Recurse -Force -ErrorAction SilentlyContinue
New-Item -Path 'yahtzeebot-submissions' -ItemType Directory

Set-Location -Path 'yahtzeebot-submissions'
New-Item -Name 'test' -ItemType Directory

Copy-Item -Path '..\mog-yahtzee\*.java' -Destination '.\test'


