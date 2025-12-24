$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"
Add-Type -AssemblyName System.Windows.Forms


function Get-String($arr) {
    ([Text.Encoding]::ASCII.GetString($arr)).Trim([char]0)
}

$edid  = Get-CimInstance -Namespace root\wmi -ClassName WmiMonitorID
$sizes = Get-CimInstance -Namespace root\wmi -ClassName WmiMonitorBasicDisplayParams
$gpu   = Get-CimInstance Win32_VideoController | Select-Object -First 1

$results = @()

foreach ($screen in [System.Windows.Forms.Screen]::AllScreens) {

    # Best-effort match (Windows does not give perfect linkage)
    $match = $edid | Select-Object -First 1
    if (-not $match) { continue }

    $size = $sizes | Where-Object {
        $_.InstanceName -eq $match.InstanceName
    }

    $width  = $screen.Bounds.Width
    $height = $screen.Bounds.Height

    $resolution = "${width}x${height}"
    $refresh    = "$($gpu.CurrentRefreshRate) Hz"

    if ($size) {
        $wIn = $size.MaxHorizontalImageSize / 2.54
        $hIn = $size.MaxVerticalImageSize / 2.54
        $diagIn = [Math]::Sqrt($wIn*$wIn + $hIn*$hIn)
        $ppi = [Math]::Sqrt($width*$width + $height*$height) / $diagIn
        $ppiStr = "{0:N1} PPI" -f $ppi
    } else {
        $ppiStr = "Unknown"
    }

    $results += [PSCustomObject]@{
        id           = $match.InstanceName
        name         = Get-String $match.UserFriendlyName
        device       = $screen.DeviceName
        resolution   = $resolution
        refreshRate  = $refresh
        pixelDensity = $ppiStr
    }
}

$results | ConvertTo-Json -Depth 3 -Compress
exit 0


