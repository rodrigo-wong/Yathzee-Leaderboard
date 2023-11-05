Set-Location -Path 'PowerShell'
Remove-Item -Path 'yahtzeebot-submissions' -Recurse -Force -ErrorAction SilentlyContinue
New-Item -Path 'yahtzeebot-submissions' -ItemType Directory

Set-Location -Path 'yahtzeebot-submissions'

# Clone every repository in Github classroom
git clone https://github.com/rodrigo-wong/yahtzeebot-ProfessorSteve.git

New-Item -Name 'test' -ItemType Directory
Copy-Item -Path '../mog-yahtzee/*.java' -Destination './test'

Get-ChildItem -Directory -Filter "yahtzeebot-*" | ForEach-Object {
    & "../test.ps1" $_.Name
}

Set-Location -Path "../../../"


