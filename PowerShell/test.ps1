Set-Location -Path "test"          # Change the working directory to "test"
Remove-Item "YahtzeeStrategy.java" # Delete YahtzeeStrategy.java

# Delete all .class
Remove-Item -Path "*.class"

# Copy YahtzeeStrategy.java from the parent directory
Copy-Item -Path "../$($args[0])/YahtzeeStrategy.java" -Destination . 

# Echo the directory name in PowerShell
Write-Host "Testing $($args[0])"

# Compile playGame.java 
javac playGame.java

# Check the exit code ($LASTEXITCODE) for the compile result
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compile failed, skipping bot."
}
else {
    Write-Host "Compile successful, scoring bot."

    # Run the Java program and outputs it to the shell
    $lastLine = $null

    java playGame | ForEach-Object {
        $_  # Output the line
        $lastLine = $_  # Update the variable with the current line
    }

    # Split the string into key-value pairs using tabs as separators
    $data = $lastLine -split "\t+"

    # Process each element to create key-value pairs
    $dataKeyValuePairs = @{}

    foreach ($element in $data) {
        $pair = $element -split ":"
    
        $key = $pair[0].Trim()
        $value = $pair[1].Trim()

        $dataKeyValuePairs[$key] = $value
    }

    # Send PUT cURL command to update student's new score
    $url = "http://localhost:8888/csep-project1/backend/updateData.php"

    $headers = @{
         'Content-Type' = 'application/json'
         'API_KEY' = 'BotSteve@2023' # Store this in an environment variable ?
    }

    $body = @{
        "publicName" = $dataKeyValuePairs["User"]
        "score" = $dataKeyValuePairs["Average Score"]
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri $url -Method Put -Body $body -Headers $headers

    $response

}