if (typeof(networkAreas) == 'undefined') {

    networkAreas = {};
    networkAreas.SERVICE_HTML_URL = "register.html?";
    networkAreas.REGISTRATION_FRAGMENT = "#registration-block";
    networkAreas.KMZ_BASE_URL = "http://redxcess.com/projects/awasr/maps/";
    networkAreas.MAP_ELEMENT_ID = "map_canvas";

    networkAreas.areas = {
        alKoudh : {
            title: "Al-Koudh",
            kmlUrlSuffix: "Al-Khod.zip",
            lat: 23.627010,
            lng: 58.206414,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alKoudh);
            }
        },
        shattiAlQurum : {
            title: "Shati-Al-Qurum",
            kmlUrlSuffix: "Shatti-Al-Qurum.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.shattiAlQurum);
            }
        },
        alMaabilah : {
            title: "Maabilah",
            kmlUrlSuffix: "Al-Mabelah.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alMaabilah);
            }
        },
        alHailNorth : {
            title: "Al Hail North",
            kmlUrlSuffix: "Al-Hail-North.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alHailNorth);
            }
        },
        alHailSouth : {
            title: "Al Hail South",
            kmlUrlSuffix: "Al-Hail-South.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alHailSouth);
            }
        },
        alMawalihNorth : {
            title: "Al Mawalih North",
            kmlUrlSuffix: "Al-Mawalih-North.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alMawalihNorth);
            }
        },
        alMawalihSouth : {
            title: "Al Mawalih South",
            kmlUrlSuffix: "Al-Mawalih-South.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alMawalihSouth);
            }
        },
        alSeeb : {
            title: "Al Seeb",
            kmlUrlSuffix: "Al-Seeb.zip",
            lat: 23.604119,
            lng: 58.4518555,
            initialize : function() {
                networkAreas.initialize(networkAreas.areas.alSeeb);
            }
        },
    }

    networkAreas.getKmzUrl = function(area) {
        return networkAreas.KMZ_BASE_URL + area.kmlUrlSuffix;
    }

    networkAreas.initialize = function(area) {
        var myOptions = {
            zoom: 14,
            center: new google.maps.LatLng(area.lat, area.lng),
            mapTypeId: google.maps.MapTypeId.SATELLITE
        }
        var map = new google.maps.Map(document.getElementById(networkAreas.MAP_ELEMENT_ID), myOptions);
        var marker = new google.maps.Marker({ map: map });
        var layer = new google.maps.KmlLayer(networkAreas.getKmzUrl(area), { preserveViewport: true, suppressInfoWindows: true});
        layer.setMap(map);

        //Resize Function
        google.maps.event.addDomListener(window, "resize", function() {
            var center = map.getCenter();
            google.maps.event.trigger(map, "resize");
            map.setCenter(center);
        });

        google.maps.event.addListener(layer, 'click', function(event) {
            showInContentWindow(event.latLng, event.featureData.description);
        });

        function showInContentWindow(position, text) {
            var content = networkAreas.getPopUpContent(text, area);

            var infowindow = new google.maps.InfoWindow({
                content: content,
                position: position
            })
            infowindow.open(map);
        }
    }

    networkAreas.getPopUpContent = function (text, area) {
        var descriptions = text.split("~");
        var fibreHub = descriptions[0];
        var networkPoint = descriptions[1];
        var serial = descriptions[2];
        var tagId = descriptions[3];
        var longitude = descriptions[4];
        var latitude = descriptions[5];

        
        var redirectUrl = networkAreas.SERVICE_HTML_URL + "tagId=" + tagId  + "&district=" + area.title + "&long=" + longitude + "&lat=" + latitude +  networkAreas.REGISTRATION_FRAGMENT;
        var content = ""+
            "<table style='font-family:Arial,Verdana,Times;font-size:12px;text-align:left;width:100%;border-spacing:0px; padding:0px 3px 3px 3px'>"+
            "<tr>"+
            "<td>"+
            "<div style='height:45px; width:64px; clear:both; background-image: url(\"images/AwasrTinyLogo.jpg\");'>"+
            "</div>"+
            "</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Network</td>"+
            "<td>Awasr Network</td>"+
            "<tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>District</td>"+
            "<td>"+area.title+"</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Longitude</td>"+
            "<td>"+longitude+"</td>"+
            "</tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>Latitude</td>"+
            "<td>"+latitude+"</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Fibre Hub</td>"+
            "<td>"+fibreHub+"</td>"+
            "</tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>Network Point</td>"+
            "<td>"+networkPoint+"</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Serial</td>"+
            "<td>"+serial+"</td>"+
            "</tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>TAG ID</td>"+
            "<td><b>"+tagId+"</b></td>"+
            "</tr>"+
            "<tr>"+
            "<td colspan='2' style='padding-top: 10px;'>Is this your home?</td>"+
            "</tr>"+
            "<tr>"+
            "<td colspan='2' style='text-align: right;'>"+
            "<a target='_blank' href='"+redirectUrl+"'>Order now</a>"+
            "</td>"+
            "</tr>"+
            "</table>";
        return content;
    }
}



