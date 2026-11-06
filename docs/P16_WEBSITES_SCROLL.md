# Lynket — P16 Websites list scroll (development candidate)

## Purpose and scope

Fix the **long-press WebHeads → Websites** dialog when 8+ sites are open: an unbounded `RecyclerView` (`wrap_content`) currently expands the dialog and can push the **Hide Overlay** command outside the visible screen. A screenshot on Galaxy S23 Ultra shows 7 items with all controls visible, but 8 items with Hide Overlay missing.

**Authorized scope:** only control the height of the Websites list. Do not change URL opening, individual close/share, Copy All, Share All, Hide Overlay broadcasts, SQLite, session persistence, background restore, Custom Tabs/Slide Over, About/Credits, application name or app identity.

## Baseline

- Source: `stable/v3.0.1` at `55b82079f7fedb3aa9e3da4cbe6680b4278969c5`.
- Development branch: `dev/3.0.1-p16-websites-scroll`.
- Internal version **3.0.1** / versionCode **64**, target/compile API **36**; Debug package and signature unchanged. No tag or release planned automatically.

## Implementation

- `activity_web_head_context.xml`: replace only the websites list tag with a `CappedWebsitesRecyclerView`. Maintain the order: header, scrollable list, Copy/Share All, Hide Overlay. Buttons stay **outside** the list.
- `CappedWebsitesRecyclerView.java`: a subclass that measures the list with `AT_MOST` rather than `EXACTLY` so short lists retain natural height. Its maximum is the lesser of a budget for roughly **seven standard website rows** (actual row height can vary with text and accessibility font sizes) and the height available after reserving the dialog header, both action rows and extra room for window edges. The budget recalculates during measurement after rotation/resize.
- `CappedWebsitesRecyclerViewTest.java`: regression checks for **3, 7, 8 and 20 sites** and for portrait/landscape-size viewports. The exact number of simultaneously visible entries depends on display height, density and title/URL wrapping; **footer visibility and scrolling take priority** over a rigid seven-row count.
- Dedicated GitHub Actions workflow copies the previously validated P16/3.0.1 checks, adds XML/action-boundary assertions and the new height-policy test, retaining direct APK upload via `archive: false`.

**Important:** the original Activity controller, adapter, button actions and data store are **byte-identical** to the stable base; the change is entirely about list measurement.

## Tests and delivery

**GitHub Actions: SUCCESS, 22/22 steps.** [Run 42220129692](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/42220129692), exact compiled commit `bf6bddf5ec05b7a68bacac546305ae48ba6a5bac`. Existing three suites plus `CappedWebsitesRecyclerViewTest` completed, alongside Android compatibility/Custom Tabs/WebHeads static checks, APK identity and v2-signature validation. Tests cover the max-height calculation for **3, 7, 8 and 20 sites** and portrait/landscape-sized viewports; **actual on-device layout was not yet verified**.

**Direct APK, confirmed real bytes (no ZIP):** [Lynket_v3.0.1_P16_WebsitesScroll_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/42220129692/artifacts/13502689269), artifact ID `13502689269`, **8,693,981 bytes**. SHA-256 `49053fac477bafe43954621a49a7af11e11a8a4931bdcd8fc714666f5cb3d002`; locally rehashed and compared after downloading. The GitHub connector labeled this file `.zip` but inspection found AndroidManifest.xml and classes*.dex at the root, i.e. the APK itself.

**User-facing delivery:** a fresh copy `/mnt/data/Lynket_v3.0.1_P16_WebsitesScroll_LIVRAISON.apk` was created via `python_user_visible.exec`, and its size, ZIP integrity, Android structure and SHA-256 were verified. The user must still confirm the downloadable ChatGPT link opens on their device.

**Pending physical checks:** test 3, 7, 8 and 20 websites, portrait/landscape, phone/tablet if available; ensure Copy All / Share All / Hide Overlay remain visible, the list can scroll, individual X closes only its own bubble and Hide retains the saved session.

Deliver the *same commit's* Debug APK as a verified **new ChatGPT attachment via `python_user_visible.exec`**, per the project's `APK_DELIVERY_WORKFLOW.md`; inspect ZIP-vs-APK content and verify size, Android structure and SHA-256. This Markdown remains passive documentation.

No stable branch, `main`, PR, release or tag change without a separate explicit user instruction.
