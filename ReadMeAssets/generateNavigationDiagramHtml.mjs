import { readFileSync, writeFileSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

const assetsDirectory = dirname(fileURLToPath(import.meta.url));
const svgPath = join(assetsDirectory, "Sprint4NavigationDiagram.svg");
const htmlPath = join(assetsDirectory, "Sprint4NavigationDiagram.html");
const diagramSvg = readFileSync(svgPath, "utf8");

if (!diagramSvg.includes("data-id=")) {
    throw new Error("El SVG debe contener identificadores de nodos para generar la vista interactiva.");
}

const html = `<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sprint 4 Navigation Map</title>
    <style>
        :root { color-scheme: dark; font-family: Inter, system-ui, sans-serif; }
        body { margin: 0; min-width: 960px; background: #111827; color: #f9fafb; }
        header { padding: 24px 32px 12px; }
        h1 { margin: 0; font-size: 24px; }
        p { margin: 8px 0 0; color: #d1d5db; }
        #status { color: #93c5fd; font-weight: 600; }
        #diagram { padding: 12px 24px 32px; overflow: auto; }
        #diagram svg { display: block; min-width: 1500px; margin: 0 auto; background: #ffffff; border-radius: 12px; }
        #diagram svg .node { cursor: pointer; transition: opacity 160ms ease, filter 160ms ease; }
        #diagram svg .flowchart-link, #diagram svg .edgeLabel { transition: opacity 160ms ease, filter 160ms ease; }
        #diagram svg .edgeLabel foreignObject { overflow: visible; }
        #diagram svg .edgeLabel div { padding: 2px 4px; background: #ffffff; border: 1px solid #cbd5e1; border-radius: 4px; box-shadow: 0 1px 3px rgba(15, 23, 42, 0.28); }
        #diagram svg .edgeLabel span { color: #0f172a !important; font-weight: 700; }
        #diagram svg.is-focused .node:not(.is-related),
        #diagram svg.is-focused .flowchart-link:not(.is-related) { opacity: 0.1; }
        #diagram svg.is-focused .node.is-related,
        #diagram svg.is-focused .flowchart-link.is-related,
        #diagram svg.is-focused .edgeLabel.is-related { opacity: 1; filter: drop-shadow(0 0 4px rgba(17, 24, 39, 0.45)); }
    </style>
</head>
<body>
    <header>
        <h1>Sprint 4 Navigation Map</h1>
        <p>Hover over a screen to isolate its incoming and outgoing navigation actions.</p>
        <p id="status" aria-live="polite">Hover over a screen to explore its relationships.</p>
    </header>
    <main id="diagram" aria-label="Interactive application navigation map">
${diagramSvg}
    </main>
    <script>
        const diagram = document.querySelector("#diagram svg");
        const nodes = Array.from(diagram.querySelectorAll(".nodes .node[data-id]"));
        const links = Array.from(diagram.querySelectorAll(".edgePaths .flowchart-link"));
        const labels = Array.from(diagram.querySelectorAll(".edgeLabels > .edgeLabel"));
        const status = document.getElementById("status");

        const svgNamespace = "http://www.w3.org/2000/svg";
        const arrowDefinitions = document.createElementNS(svgNamespace, "defs");
        diagram.prepend(arrowDefinitions);

        const markerIdFor = (color) => "arrow-" + color.replace(/[^a-z0-9]/gi, "");
        const markerFor = (color) => {
            const markerId = markerIdFor(color);
            const existingMarker = document.getElementById(markerId);

            if (existingMarker) return markerId;

            const marker = document.createElementNS(svgNamespace, "marker");
            marker.setAttribute("id", markerId);
            marker.setAttribute("viewBox", "0 0 10 10");
            marker.setAttribute("refX", "8");
            marker.setAttribute("refY", "5");
            marker.setAttribute("markerWidth", "8");
            marker.setAttribute("markerHeight", "8");
            marker.setAttribute("orient", "auto");

            const tip = document.createElementNS(svgNamespace, "path");
            tip.setAttribute("d", "M 0 0 L 10 5 L 0 10 z");
            tip.style.setProperty("fill", color, "important");
            tip.style.setProperty("stroke", color, "important");
            marker.style.setProperty("fill", color, "important");
            marker.style.setProperty("stroke", color, "important");
            marker.append(tip);
            arrowDefinitions.append(marker);
            return markerId;
        };

        links.forEach((link) => {
            const color = getComputedStyle(link).stroke;
            link.setAttribute("marker-end", "url(#" + markerFor(color) + ")");
        });

        const relatedNodeId = (link, prefix) => {
            const className = Array.from(link.classList).find((name) => name.startsWith(prefix));
            return className ? className.slice(prefix.length) : null;
        };

        const clearFocus = () => {
            diagram.classList.remove("is-focused");
            [...nodes, ...links, ...labels].forEach((element) => element.classList.remove("is-related"));
            status.textContent = "Hover over a screen to explore its relationships.";
        };

        const focusNode = (nodeId) => {
            const relatedLinks = links.filter((link) =>
                relatedNodeId(link, "LS-") === nodeId || relatedNodeId(link, "LE-") === nodeId
            );
            const connectedNodeIds = new Set([nodeId]);

            relatedLinks.forEach((link) => {
                connectedNodeIds.add(relatedNodeId(link, "LS-"));
                connectedNodeIds.add(relatedNodeId(link, "LE-"));
            });

            diagram.classList.add("is-focused");
            nodes.forEach((node) => node.classList.toggle("is-related", connectedNodeIds.has(node.dataset.id)));
            links.forEach((link) => link.classList.toggle("is-related", relatedLinks.includes(link)));
            labels.forEach((label, index) => label.classList.toggle("is-related", relatedLinks.includes(links[index])));
            status.textContent = relatedLinks.length + " navigation relationship(s) for " + nodeId + ".";
        };

        nodes.forEach((node) => {
            node.addEventListener("mouseenter", () => focusNode(node.dataset.id));
            node.addEventListener("focus", () => focusNode(node.dataset.id));
            node.setAttribute("tabindex", "0");
        });

        diagram.addEventListener("mouseleave", clearFocus);
    </script>
</body>
</html>`;

writeFileSync(htmlPath, html);
