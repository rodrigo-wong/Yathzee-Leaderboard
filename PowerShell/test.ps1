Set-Location -Path "test"          # Change the working directory to "test"
Remove-Item "YahtzeeStrategy.java" # Delete YahtzeeStrategy.java

# Delete all .class
Remove-Item -Path "*.class"

# Copy YahtzeeStrategy.java from the parent directory
Copy-Item -Path "../$($args[0])/YahtzeeStrategy.java" -Destination . 

# Echo the value of %1 (or $args[0]) in PowerShell
Write-Host "Testing $($args[0])"

# Compile playGame.java 
javac playGame.java

# Check the exit code ($LASTEXITCODE) for the compile result
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compile failed, skipping bot."
} else {
    Write-Host "Compile successful, scoring bot."

    # Run the Java program and outputs it to the shell
    $lastLine = $null

    java playGame | ForEach-Object {
        $_  # Output the line
        $lastLine = $_  # Update the variable with the current line
    }

    # Split the input string by whitespace and trim each element
    $data = $lastLine -split '\t+'

    $publicName = ($data[0] -split ':')[1].Trim() # Trim and store user's name
    $score = ($data[4] -split ":")[1].Trim() # Trim and store average score

    # Do cURL command to update student's new score

    # Specify the URL
    $url = "http://localhost:8888/csep-project1/backend/updateData.php"

    # Specify the data to send in the request
    $headers = @{
        'Content-Type' = 'application/json'
        'API_KEY' = 'SteveBot@2023'
    }

    $body = @{
        "publicName" = $publicName
        "score" = $score
    } | ConvertTo-Json

    # Send the PUT request
    $response = Invoke-RestMethod -Uri $url -Method Put -Body $body -Headers $headers

    # Display the response
    $response

}